package com.jin.controller;

import com.jin.*;

import com.jin.common.CommonResponse;
import com.jin.entity.Order;
import com.jin.entity.Product;

import com.jin.service.IAccountService;
import com.jin.utils.GlobalExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * creat by Jin 2020/8/17 16:37
 *
 * @Description:
 */
@RestController
@RequestMapping("/rpc")
public class OrderController {



    @Autowired
    private OrderService orderService;

    @Autowired
    private IAccountService accountService;

    private Lock lock = new ReentrantLock();

    @GetMapping("/6666")
    public CommonResponse order() {

        for (int i = 0; i < 10000; i++) {

            new Thread(() -> {
                lock.lock();
                try {
                    Product product = accountService.trytest();
                    System.out.println(product + "    " + Thread.currentThread().getName());
                    try {
                        TimeUnit.NANOSECONDS.sleep(600);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }, "AAA" + i).start();
        }
        return new CommonResponse();
    }


    @GetMapping("/creat")
    public CommonResponse creat(Order order) {
        for (int i = 0; i < 10000; i++) {
            new Thread(() -> {
                orderService.createOrder(order);
            },"name").start();

        }
       return   CommonResponse.okResult("Ok");
    }

    @GetMapping("/creat2")
    public CommonResponse creat2() {
        int i;
        for (i = 0; i < 10000; i++) {


            try {
                TimeUnit.MICROSECONDS.sleep(600);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        return new CommonResponse();
    }


    @GetMapping("/gc")
    public String func() {
        ExecutorService executorServiceForRank = GlobalExecutor.getExecutorServiceForRank();
        System.out.println(executorServiceForRank);
        System.gc();
        System.out.println(GlobalExecutor.getExecutorServiceForRank());
        return "gc";

    }


    @GetMapping("/try")
    public String trytest() {
        for (int i = 0; i < 10000; i++) {
            new Thread(() -> {
                Product product = accountService.trytest();
                System.out.println(product + "    " + Thread.currentThread().getName());
            }, "name").start();
        }
        return "操作成功";
    }
}
