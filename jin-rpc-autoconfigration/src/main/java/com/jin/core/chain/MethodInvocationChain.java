package com.jin.core.chain;

import com.jin.cach.ClientsCashManager;
import com.jin.dto.RPCDto;
import com.jin.utils.ApplicationUtils;
import com.jin.utils.GlobalExecutor;
import com.jin.utils.RpcThreadFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * creat by Jin 2020/9/5 22:02
 *
 * @Description:
 */
public class MethodInvocationChain implements FiterJoinpoin {
    private int currentfilterIndex = -1;

    private List<RpcFilter> filters = new ArrayList<>();

    private RPCDto dto;

    private final static int CPUCORES=Runtime.getRuntime().availableProcessors();
    private static ExecutorService RPCEXECUTOR=new ThreadPoolExecutor(
            CPUCORES, //初始化线程个数
            CPUCORES,     //cpu核心数字
            2L,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(500),  //设置等待队列长度
            new RpcThreadFactory(),
            new ThreadPoolExecutor.CallerRunsPolicy());

    public MethodInvocationChain(List<RpcFilter> filters, RPCDto dto) {
        this.filters = filters;
        this.dto = dto;
    }

    @Override
    public void processClientSend() throws Throwable {
        if (this.currentfilterIndex == this.filters.size() - 1) {
            return;
        }
        RpcFilter rpcFilter = this.filters.get(++this.currentfilterIndex);
        rpcFilter.invokeClinentSend(dto, this);
    }


    @Override
    public Object processClientReceive() throws Throwable {
        if (this.currentfilterIndex == this.filters.size() - 1) {
            return ClientRecevieInvoke();
        }
        RpcFilter rpcFilter = this.filters.get(++this.currentfilterIndex);
        return rpcFilter.invokeClinentReceive(dto, this);
    }


    @Override
    public Object processServerReceive() throws Throwable {
        if (this.currentfilterIndex == this.filters.size() - 1) {
            return serverInvoke();
        }
        RpcFilter rpcFilter = this.filters.get(++this.currentfilterIndex);
        return rpcFilter.invokeServerReceive(dto, this);
    }


    public Object serverInvoke() {
        try {
            String beanName = dto.getClazzName();
            String methodName = dto.getMethodName();

            Class[] parametetypes = dto.getParameterTypes();
            Object[] args = dto.getArgs();
            Class<?> returnType = dto.getReturnType();

            //这一步是从 spring容器中获取  或者自动生成
            //   Object newInstance = Class.forName(clazzName).newInstance();
            //Object newInstance = ApplicationContextUtils.getBean(beanName);

            Object newInstance = ApplicationUtils.getBean(dto.getBeanClass());
            Method method = newInstance.getClass().getDeclaredMethod(methodName, parametetypes);
            method.setAccessible(true);

            Object returnValue = method.invoke(newInstance, args);
            return returnValue;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("process 消息处理出现异常");
        }
    }

    private Object ClientRecevieInvoke() {
        ClientsCashManager.invokeMap.put(dto.getId(), CompletableFuture.supplyAsync(() -> {
            return dto;
        }, RPCEXECUTOR));
        ClientsCashManager.putResultValue(dto.getId(), dto);


        return dto.getReturnType();
    }
}
