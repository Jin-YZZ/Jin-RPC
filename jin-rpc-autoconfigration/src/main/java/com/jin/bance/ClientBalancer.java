package com.jin.bance;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * creat by Jin 2020/9/4 10:01
 *
 * @Description:
 */
public class ClientBalancer {

    public static AtomicInteger integer=new AtomicInteger(0);

    public static final Integer NUMS=5;

    public static final Integer NUM10=NUMS*NUMS*NUMS*NUMS*NUMS*NUMS*NUMS*NUMS*NUMS*NUMS*NUMS;

    public static final String MIDDLE="##";


    /**
     * 轮询
     * @return
     */
    public static Integer loadbance(){
        int increment = integer.getAndIncrement();
        if (increment>=NUM10){
            integer.set(1);
        }
        return integer.getAndIncrement()%NUMS;
    }

}

