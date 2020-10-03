package com.jin.chuanshu;


import com.jin.Order;
import io.netty.buffer.ByteBuf;

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

     public static <T>T getSub(Class<?> clazz) throws IllegalAccessException, InstantiationException {

         InvocationHandler handler=new InvocationHandler() {
             @Override
             public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {


                 String methodName = method.getName();
                 String clazzName = clazz.getName();
                 Class<?>[] parameterTypes = method.getParameterTypes();
                 Class<?> returnType = method.getReturnType();
                 RPCDto dto = new RPCDto();
                 dto.setClazzName(clazzName);
                 dto.setMethodName(methodName);
                 dto.setParameterTypes(parameterTypes);
                 dto.setArgs(args);
                 dto.setReturnType(returnType);
                 byte[] serialize = HelloHessian.serialize(dto);
                 dto= (RPCDto) HelloHessian.deserialize(serialize);
                 System.out.println(dto.getArgs().length );

                 return new Order() ;
             }
         };

         Object instance = Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), handler);
         return (T) instance;
     }
}
