package com.jin.rpc05;

import com.jin.Order;

import java.io.*;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * creat by Jin 2020/8/17 10:04
 *
 * @Description:
 */
public class Server {

    private static  boolean flag=true;
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(8888);

        while (flag){
            Socket accept = serverSocket.accept();
            process(accept);
            accept.close();
        }
        serverSocket.close();

    }

    private static void process(Socket socket)throws Exception{
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        ObjectInputStream ois = new ObjectInputStream(inputStream);
        ObjectOutputStream oos=new ObjectOutputStream(outputStream);

        String clazzName = ois.readUTF();
        String methodName = ois.readUTF();
        Class[] parametetypes  = (Class[]) ois.readObject();
        Object[] args = (Object[]) ois.readObject();
        Class<?> returnType = (Class<?>) ois.readObject();

        //这一步是从 spring容器中获取  或者自动生成
        Object newInstance = Class.forName(clazzName).newInstance();

        Method method = newInstance.getClass().getDeclaredMethod(methodName, parametetypes);
        method.setAccessible(true);

        Object returnValue = returnType.newInstance();
        returnValue =method.invoke(newInstance, args);
        oos.writeObject(returnValue);

        inputStream.close();
        outputStream.close();
        ois.close();
        oos.close();

    }
}
