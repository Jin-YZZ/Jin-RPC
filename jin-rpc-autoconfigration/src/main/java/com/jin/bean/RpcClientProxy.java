package com.jin.bean;

/**
 * creat by Jin 2020/9/14 07:44
 *
 * @Description:
 */


import com.jin.anno.RpcServer;
import com.jin.cach.ClientsCashManager;
import com.jin.core.chain.FilterChainManager;
import com.jin.dto.RPCDto;
import com.jin.rule.IRpcRule;
import com.jin.utils.ApplicationContextUtils;
import com.netflix.loadbalancer.Server;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import static com.jin.discovery.RpcServerNacosDiscovery.SUFERIX;


/**
 * 动态代理，需要注意的是，这里用到的是JDK自带的动态代理，代理对象只能是接口，不能是类
 *
 * @author Jin
 */

public class RpcClientProxy<T> implements InvocationHandler {

    private Class<T> interfaceType;

    private RpcServer annotation;

    public static ExecutorService singtonExecytor=new ThreadPoolExecutor(
            1, //初始化线程个数
            1,     //cpu核心数字
            2L,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(500),  //设置等待队列长度
            new ThreadPoolExecutor.CallerRunsPolicy());

    public RpcClientProxy(Class<T> interfaceType, RpcServer annotation) {
        this.interfaceType = interfaceType;
        this.annotation = annotation;
    }

    private FilterChainManager filterChainManager;
    private IRpcRule rpcrule;

    private ClientsCashManager clientsCashManager;

    private Lock lock = new ReentrantLock();


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Server server = getRpcrule().choose(SUFERIX + annotation.sever());
        RPCDto dto = initRpcDto(method, interfaceType, args);
        getFilterChainManager().handleClientSend(dto);

        lock.lock();
        try {
            getClientsCashManager().sendMessage(annotation.sever(), server, dto);
            Object returnValue = getReturnValue(dto);

            return returnValue;
        } finally {
            lock.unlock();
        }
    }

    public Object getReturnValue(RPCDto dto) throws ExecutionException, InterruptedException {
        Object o = singtonExecytor.submit(() -> {
            Object retrunValue = null;
            if (!dto.getReturnType().getName().equals("void")) {
                retrunValue = ClientsCashManager.getRetrunValue(dto.getId(), new AtomicInteger(0));
            }
            return retrunValue;
        }).get();
        return o;
    }

    public RPCDto initRpcDto(Method method, Class<?> beanclazz, Object[] args) {
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        Class<?> returnType = method.getReturnType();
        RPCDto dto = new RPCDto();
        UUID uuid = UUID.randomUUID();
        dto.setId(uuid.toString());

        dto.setBeanClass(beanclazz);
        dto.setMethodName(methodName);
        dto.setParameterTypes(parameterTypes);
        dto.setArgs(args);
        dto.setReturnType(returnType);
        return dto;
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