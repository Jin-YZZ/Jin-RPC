package com.jin.core;

import com.jin.anno.RpcServer;
import com.jin.properties.RpcServerConfigProperties;

import com.jin.utils.SpringProxyUtils;
import lombok.SneakyThrows;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.Advisor;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;

import java.lang.reflect.Method;

/**
 * creat by Jin 2020/9/6 10:20
 *
 * @Description:
 */
public class GlobalRpcClinentScanner extends AbstractAutoProxyCreator {

    private MethodInterceptor interceptor;

    private RpcServerConfigProperties rpcServerConfigProperties;

    public GlobalRpcClinentScanner(RpcServerConfigProperties rpcServerConfigProperties) {
        this.rpcServerConfigProperties = rpcServerConfigProperties;
    }

    @Override
    protected Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName, TargetSource customTargetSource) throws BeansException {
        return new Object[]{interceptor};
    }

    @SneakyThrows
    @Override
    protected Object wrapIfNecessary(Object bean, String beanName, Object cacheKey) {

        if (shouldEnhancer(bean, beanName)) {
            //含有注解进行方法增强
            interceptor = new RpcClientProxy(beanName, rpcServerConfigProperties, bean,SpringProxyUtils.findTargetInsterface(bean));
            if (!AopUtils.isAopProxy(bean)) {
                bean = super.wrapIfNecessary(bean, beanName, cacheKey);
            } else {
                AdvisedSupport advised = SpringProxyUtils.getAdvisedSupport(bean);
                Advisor[] advisor = buildAdvisors(beanName, getAdvicesAndAdvisorsForBean(null, null, null));
                for (Advisor avr : advisor) {
                    advised.addAdvisor(advised.getAdvisors().length, avr);
                }
            }
        }

        return bean;
    }

    public boolean shouldEnhancer(Object bean, String beanName) {

        Class<?> targetClass = null;
        Class<?> targetInsterface = null;
        try {
            targetClass = SpringProxyUtils.findTargetClass(bean);
            targetInsterface = SpringProxyUtils.findTargetInsterface(bean);
        } catch (Exception e) {
            throw new RuntimeException("SpringProxyUtils.findTargetClass：执行出错,beanName: " + beanName);
        }

        if (shouldEnhancer(targetClass) || shouldEnhancer(targetInsterface)) {
            return true;
        }

        return false;

    }

    public Boolean shouldEnhancer(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            RpcServer rpcServer = declaredMethod.getDeclaredAnnotation(RpcServer.class);
            if (rpcServer != null) {
                if (!rpcServerConfigProperties.getRpcServername().equals(rpcServer.sever())) {
                    return true;
                }
            }
        }
        return false;

    }


    public Class<?> getBeanClazz(Object bean) {
        Class<?>  targetClass = SpringProxyUtils.findTargetClass(bean);
        Class<?>  targetInsterface = SpringProxyUtils.findTargetInsterface(bean);
        return targetClass!=null? targetClass:targetInsterface;
    }


}
