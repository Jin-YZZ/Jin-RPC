package com.jin.rpc05;




import java.io.*;
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

     public static <T>T getSub(Class<?> clazz) throws IllegalAccessException, InstantiationException {

         InvocationHandler handler=new InvocationHandler() {
             @Override
             public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                 Socket socket = new Socket("127.0.0.1", 8888);
                 ObjectOutputStream  oos=new ObjectOutputStream(socket.getOutputStream());
                 String methodName = method.getName();
                 String clazzName = clazz.getName();
                 Class<?>[] parameterTypes = method.getParameterTypes();
                 Class<?> returnType = method.getReturnType();
                 oos.writeUTF(clazzName);
                 oos.writeUTF(methodName);
                 oos.writeObject(parameterTypes);
                 oos.writeObject(args);
                 oos.writeObject(returnType);
                 oos.flush();

                 ObjectInputStream ois=new ObjectInputStream(socket.getInputStream());
                 Object newInstance = returnType.newInstance();
                 newInstance=ois.readObject();

                 oos.close();
                 ois.close();
                 socket.close();
                 return newInstance;
             }
         };

         Object instance = Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), handler);
         return (T) instance;
     }
}
