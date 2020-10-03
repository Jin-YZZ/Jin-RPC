package com.jin.rcp06_cglib;

import com.jin.Order;
import com.jin.OrderService;
import com.jin.Product;
import com.jin.ProductService;

import java.util.concurrent.TimeUnit;

/**
 * creat by Jin 2020/8/17 10:04
 *
 * @Description:
 */
public class Client {
    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 1000; i++) {
            ProductService sub = Stub.getSub(new ProductServiceImpl());
            Product product = sub.findbyId(1);
            try {
                TimeUnit.NANOSECONDS.sleep(4);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(product);
            OrderService orderService = Stub.getSub(new OrderServiceImpl());
            Order order = orderService.findbyId(1);
            try {
                TimeUnit.NANOSECONDS.sleep(4);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(order);

        }
    }


}
