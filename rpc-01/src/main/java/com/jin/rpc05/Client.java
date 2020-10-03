package com.jin.rpc05;

import com.jin.Order;
import com.jin.OrderService;
import com.jin.Product;
import com.jin.ProductService;
import sun.security.provider.Sun;

import java.util.concurrent.TimeUnit;

/**
 * creat by Jin 2020/8/17 10:04
 *
 * @Description:
 */
public class Client {
    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 1000; i++) {
            ProductService sub = Stub.getSub(ProductServiceImpl.class);
            Product product = sub.findbyId(1);
            try {
                TimeUnit.NANOSECONDS.sleep(4);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(product);
            OrderService orderService = Stub.getSub(OrderServiceImpl.class);
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
