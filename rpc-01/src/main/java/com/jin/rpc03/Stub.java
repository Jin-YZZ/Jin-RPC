package com.jin.rpc03;

import com.jin.Order;
import com.jin.OrderService;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

/**
 * creat by Jin 2020/8/17 10:28
 *
 * @Description:
 */
public class Stub {

    public Order findByid(Integer id)throws Exception{
        Socket socket = new Socket("127.0.0.1", 8888);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();//创建字节数组用于存放二进制数字
        DataOutputStream dos = new DataOutputStream(baos);//将  所有类型可以转化为二进制数
        dos.writeInt(123);

        socket.getOutputStream().write(baos.toByteArray());
        socket.getOutputStream().flush();

        DataInputStream dis = new DataInputStream(socket.getInputStream());
        int reviceveId = dis.readInt();
        String name = dis.readUTF();

        Order order = new Order(reviceveId, name);


        dos.close();
        socket.close();
        dis.close();
        return order;
    }

     public static OrderService getSub(){

         InvocationHandler handler=new InvocationHandler() {
             @Override
             public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                 Socket socket = new Socket("127.0.0.1", 8888);
                 ByteArrayOutputStream baos = new ByteArrayOutputStream();//创建字节数组用于存放二进制数字
                 DataOutputStream dos = new DataOutputStream(baos);//将  所有类型可以转化为二进制数
                 dos.writeInt(123);

                 socket.getOutputStream().write(baos.toByteArray());
                 socket.getOutputStream().flush();

                 DataInputStream dis = new DataInputStream(socket.getInputStream());
                 int reviceveId = dis.readInt();
                 String name = dis.readUTF();

                 Order order = new Order(reviceveId, name);


                 dos.close();
                 socket.close();
                 dis.close();
                 return order;
             }
         };

         Object instance = Proxy.newProxyInstance(OrderService.class.getClassLoader(), new Class[]{OrderService.class}, handler);
         return (OrderService) instance;
     }
}
