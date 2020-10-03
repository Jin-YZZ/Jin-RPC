package com.jin.core;

import com.alibaba.nacos.api.exception.NacosException;
import com.jin.anno.RpcServer;
import com.jin.bance.ClientBalancer;
import com.jin.cach.ClientsCashManager;
import com.jin.client.RpcNettyClient;
import com.jin.core.chain.FilterChainManager;
import com.jin.dto.RPCDto;
import com.jin.properties.RpcServerConfigProperties;
import com.jin.rule.IRpcRule;
import com.jin.utils.ApplicationContextUtils;
import com.jin.utils.SpringProxyUtils;
import com.netflix.loadbalancer.Server;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.jin.discovery.RpcServerNacosDiscovery.SUFERIX;

/**
 * creat by Jin 2020/9/6 10:26
 *
 * @Description:
 */
public class RpcClientProxy implements MethodInterceptor {

    private FilterChainManager filterChainManager;
    private IRpcRule rpcrule;
    private String beanName;
    private RpcServerConfigProperties rpcServerConfigProperties;
    private Object bean;
    private Class<?> beaclazz;
    private ClientsCashManager clientsCashManager;

    private Lock lock = new ReentrantLock();

    public RpcClientProxy(String beanName, RpcServerConfigProperties rpcServerConfigProperties, Object bean, Class<?> beaclazz) {
        this.beanName = beanName;
        this.rpcServerConfigProperties = rpcServerConfigProperties;
        this.bean = bean;
        this.beaclazz = beaclazz;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        List<Method> methods = getMethods(invocation);

        //没有改注解不走这里
        if (shouldskip(methods)) {
            return invocation.proceed();
        }
        RpcServer annotation = getRpcServerAnno(methods);
        Server server = getRpcrule().choose(SUFERIX + annotation.sever());
        RPCDto dto = initRpcDto(methods.get(0), beanName, invocation.getArguments());
        getFilterChainManager().handleClientSend(dto);

        lock.lock();
        try {
            getClientsCashManager().sendMessage(annotation.sever(), server, dto);
            Object returnValue = getReturnValue(dto);
            System.out.println("999999999999999999999999999999999");
            return returnValue;
        } finally {
            lock.unlock();
        }
        //发送消息
    }

    public Object getReturnValue(RPCDto dto) {
        Object retrunValue = null;
        if (!dto.getReturnType().getName().equals("void")) {
            retrunValue = ClientsCashManager.getRetrunValue(dto.getId(), new AtomicInteger(0));
        }
        return retrunValue;
    }

    public RPCDto initRpcDto(Method method, String beanName, Object[] args) {
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        Class<?> returnType = method.getReturnType();
        RPCDto dto = new RPCDto();
        UUID uuid = UUID.randomUUID();
        dto.setId(uuid.toString());
        dto.setClazzName(beanName);


        dto.setBeanClass(beaclazz);
        dto.setMethodName(methodName);
        dto.setParameterTypes(parameterTypes);
        dto.setArgs(args);
        dto.setReturnType(returnType);
        return dto;
    }

    private RpcServer getRpcServerAnno(List<Method> methods) {
        for (Method method : methods) {
            RpcServer annotation = method.getDeclaredAnnotation(RpcServer.class);
            if (annotation != null) {
                return annotation;
            }
        }
        return null;
    }

    public Boolean shouldskip(List<Method> methods) {
        for (Method method : methods) {
            RpcServer declaredAnnotation = method.getDeclaredAnnotation(RpcServer.class);
            if (declaredAnnotation != null && !declaredAnnotation.sever().equals(rpcServerConfigProperties.getRpcServername())) {
                return false;
            }
        }
        return true;
    }


    private List<Method> getMethods(MethodInvocation invocation) {
        List<Method> methods = new ArrayList<>();
        Class<?> targetClass = SpringProxyUtils.findTargetClass(bean);
        Method method = getMethod(targetClass, invocation);
        method.setAccessible(true);
        methods.add(method);
        Class<?> insterface = SpringProxyUtils.findTargetInsterface(bean);

        if (insterface != null) {
            methods.add(getMethod(insterface, invocation));
        }
        return methods;
    }

    private Method getMethod(Class<?> clazz, MethodInvocation invocation) {
        try {
            return clazz.getMethod(invocation.getMethod().getName(), invocation.getMethod().getParameterTypes());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException("没有该方法com.jin.core.RpcClientProxy.getMethod");
        }
    }


    public FilterChainManager getFilterChainManager() {
        return filterChainManager != null ? filterChainManager : (filterChainManager = ApplicationContextUtils.getBeanByClazz(FilterChainManager.class));
    }

    public IRpcRule getRpcrule() {
        return rpcrule != null ? rpcrule : (rpcrule = ApplicationContextUtils.getBeanByClazz(IRpcRule.class));
    }

    public ClientsCashManager getClientsCashManager() {
        return clientsCashManager != null ? clientsCashManager : (clientsCashManager = ApplicationContextUtils.getBeanByClazz(ClientsCashManager.class));
    }
}
