package com.jin.rpc02;

import com.jin.Order;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
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
}
