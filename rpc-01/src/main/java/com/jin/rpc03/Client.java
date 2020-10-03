package com.jin.rpc03;

import com.jin.Order;
import com.jin.OrderService;

/**
 * creat by Jin 2020/8/17 10:04
 *
 * @Description:
 */
public class Client {
    public static void main(String[] args) throws Exception {
        Stub stub = new Stub();
        OrderService sub = stub.getSub();
        Order byid = stub.findByid(1);
        System.out.println(byid);
    }


}
