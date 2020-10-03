package com.jin.bean;


import com.jin.anno.RpcServer;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.InvocationHandler;


/**
 * 接口实例工厂，这里主要是用于提供接口的实例对象
 *
 * @param <T>
 * @author Jin
 */
public class RpcClientFactory<T> implements FactoryBean<T> {

    private Class<T> interfaceType;


    public RpcClientFactory(Class<T> interfaceType ) {
        this.interfaceType = interfaceType;
    }

    @Override
    public T getObject() throws Exception {
        //这里主要是创建接口对应的实例，便于注入到spring容器中
        InvocationHandler handler = new RpcClientProxy<>(interfaceType,interfaceType.getDeclaredAnnotation(RpcServer.class));
        RpcClientProxyCjlib<T> proxyCjlib = new RpcClientProxyCjlib<>(interfaceType, interfaceType.getDeclaredAnnotation(RpcServer.class));
        return (T) proxyCjlib.getInstance(interfaceType);
    }

    @Override
    public Class<T> getObjectType() {
        return interfaceType;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}