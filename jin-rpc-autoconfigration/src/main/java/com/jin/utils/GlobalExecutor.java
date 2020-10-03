package com.jin.utils;

import java.util.concurrent.*;

/**
 * creat by Jin 2020/9/3 06:24
 *
 * @Description:
 */
public class GlobalExecutor {
    private static int CPUCORES = Runtime.getRuntime().availableProcessors();

    public static ExecutorService SINGLETON=new ThreadPoolExecutor(
            1, //初始化线程个数
            1,     //cpu核心数字
            2L,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(500),  //设置等待队列长度
            new ThreadPoolExecutor.CallerRunsPolicy());

    public  static  ScheduledExecutorService scheduledExecutor=new ScheduledThreadPoolExecutor(CPUCORES);

    public static ExecutorService RPCEXECUTOR=new ThreadPoolExecutor(
            CPUCORES, //初始化线程个数
            CPUCORES,     //cpu核心数字
            2L,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(500),  //设置等待队列长度
            new RpcThreadFactory(),
            new ThreadPoolExecutor.CallerRunsPolicy());



    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            RPCEXECUTOR.submit(() -> {
                System.out.println(Thread.currentThread().getName());
                try {
                    TimeUnit.NANOSECONDS.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
