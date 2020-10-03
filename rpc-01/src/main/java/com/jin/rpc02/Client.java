package com.jin.rpc02;

import com.jin.Order;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

/**
 * creat by Jin 2020/8/17 10:04
 *
 * @Description:
 */
public class Client {
    public static void main(String[] args) throws Exception {
        Stub stub = new Stub();
        Order order = stub.findByid(1);
        System.out.println(order);
    }


}
