package com.jin.rcp06_cglib;


import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

     public static <T>T getSub(Object taget) throws IllegalAccessException, InstantiationException {
         MethodInterceptor methodInterceptor = new MethodInterceptor() {
             @Override
             public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                 Socket socket = new Socket("127.0.0.1", 8888);
                 ObjectOutputStream  oos=new ObjectOutputStream(socket.getOutputStream());
                 String methodName = method.getName();
                 String clazzName = taget.getClass().getName();
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


         //创建增强器
         Enhancer enhancer = new Enhancer();        //创建增强器
         enhancer.setSuperclass(taget.getClass());            //设置原本方法
         enhancer.setCallback(methodInterceptor);  //设置方法拦截器
         return (T) enhancer.create();
     }
}
