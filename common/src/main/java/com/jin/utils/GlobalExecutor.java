package com.jin.utils;

import java.util.concurrent.*;

/**
 * creat by Jin 2020/8/16 17:24
 *
 * @Description:
 */
@SuppressWarnings("all")
public class GlobalExecutor {

    private  static  int CPUCORES=Runtime.getRuntime().availableProcessors();

    private static  ExecutorService executorServiceForRank;

    public static ExecutorService singltonexecutor=new  ThreadPoolExecutor(
            1,
            1,
            2L,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(5),  //设置等待队列长度
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy());


     private static class InnerHoldClass{
         private static ExecutorService executorService=new ThreadPoolExecutor(
                 4, //初始化线程个数
                 CPUCORES,     //cpu核心数字
                 2L,
                 TimeUnit.SECONDS,
                 new LinkedBlockingDeque<>(50),  //设置等待队列长度
                 Executors.defaultThreadFactory(),
                 new ThreadPoolExecutor.AbortPolicy()); //AbortPolicy 会报错 默认
         // CallerRunsPolicy 回馈  由调用线程处理该任务抛给上一级线程处理
         //DiscardPolicy可以丢弃不报错
         //抛弃任务等待最久的任务DiscardOldestPolicy

     }
     public static ExecutorService getExecutorServiceForRank(){
         return new InnerHoldClass().executorService;
     }

     public static ExecutorService getSingltonexecutor(){
         return singltonexecutor;
     }


}
