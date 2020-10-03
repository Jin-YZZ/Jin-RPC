package com.jin.bean;

import com.jin.anno.RpcServer;
import com.jin.cach.ClientsCashManager;
import com.jin.core.chain.FilterChainManager;
import com.jin.dto.RPCDto;
import com.jin.error.RpcException;
import com.jin.rule.IRpcRule;
import com.jin.utils.ApplicationContextUtils;
import com.jin.utils.GlobalExecutor;
import com.netflix.loadbalancer.Server;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.jin.discovery.RpcServerNacosDiscovery.SUFERIX;


/**
 * creat by Jin 2020/9/14 19:31
 *
 * @Description:
 */
public class RpcClientProxyCjlib<T> implements MethodInterceptor {

    private Log log = LogFactory.getLog(RpcClientProxyCjlib.class);

    private Class<T> interfaceType;

    private RpcServer annotation;


    private FilterChainManager filterChainManager;

    private IRpcRule rpcrule;

    private ClientsCashManager clientsCashManager;

    private Lock lock = new ReentrantLock();

    public RpcClientProxyCjlib(Class<T> interfaceType, RpcServer annotation) {
        this.interfaceType = interfaceType;
        this.annotation = annotation;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        Server server = getRpcrule().choose(SUFERIX + annotation.sever());
        RPCDto dto = initRpcDto(method, interfaceType, objects);
        getFilterChainManager().handleClientSend(dto);

        lock.lock();
        try {
            getClientsCashManager().sendMessage(annotation.sever(), server, dto);
            waitUtilRecive(dto.getId());

        } finally {
            lock.unlock();
        }
        CompletableFuture<RPCDto> future = ClientsCashManager.invokeMap.get(dto.getId());
        return processReturnValue(future);

    }

    private void waitUtilRecive(String id) {
        int count = 0;
        while (ClientsCashManager.invokeMap.get(id) == null) {
            try {
                TimeUnit.NANOSECONDS.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (count == 1000) {
                throw new RuntimeException("消费超时。。。id：" + id);
            }
        }
    }

    private Object processReturnValue(CompletableFuture<RPCDto> future) {
        future.thenApply((el) -> {
            if (el.getReturnValue() == null && !el.getReturnType().getName().equals("void")) {
                throw new RuntimeException("消费超时。。。id：" + el.getId());
            } else if (!el.isOK()) {
                throw new RuntimeException(el.getErrorMessage());
            } else {
                return el;
            }
        }).handle((el, throwable) -> {
            if (throwable != null) {
                if (throwable instanceof RuntimeException) {
                    RuntimeException ex = (RuntimeException) throwable;
                    throw ex;
                } else {
                    throwable.printStackTrace();
                }
            }
            return el;
        });
        try {
            RPCDto rpcDto = future.get();
            ClientsCashManager.invokeMap.remove(rpcDto.getId());
            return rpcDto.getReturnValue();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Object getInstance(Class<?> clazz) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(this);
        return enhancer.create();
    }


    public Object getReturnValue(String Id) throws ExecutionException, InterruptedException {

        RPCDto dto = ClientsCashManager.getRetrunValue(Id, new AtomicInteger(0));
        checkReturnValue(dto, Id);

        return dto.getReturnValue();
    }

    public void checkReturnValue(RPCDto dto, String id) {
        if (dto == null) {
            throw new RuntimeException("Id:" + id + "消费超时进行日记记录");
        }

        if (!dto.isOK()) {
            RpcException.throwRpcException(dto.getErrorMessage());
        }


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
