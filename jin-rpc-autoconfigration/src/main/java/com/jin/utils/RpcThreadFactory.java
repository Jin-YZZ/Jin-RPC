package com.jin.utils;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * creat by Jin 2020/9/17 17:19
 *
 * @Description:
 */
public class RpcThreadFactory implements ThreadFactory {
    private final static AtomicInteger integer=new AtomicInteger(0);

    private final static String NAMESUFFER="JIN-RPC";
    private final static String THREAD="Thread";


    @Override
    public Thread newThread(Runnable r) {
        String threadName=THREAD+" - "+NAMESUFFER+" - "+integer.getAndIncrement();
        Thread thread = new Thread(r,threadName);
        thread.setDaemon(false);//守护线程
        return thread;
    }

    public RpcThreadFactory() {

    }
}
