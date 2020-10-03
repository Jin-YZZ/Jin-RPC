/*
 *  Copyright 1999-2019 Seata.io Group.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.jin.utils;

import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.target.EmptyTargetSource;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Proxy tools base on spring
 *
 * @author zhangsen
 */
public class SpringProxyUtils {

    private SpringProxyUtils() {

    }

    public static Boolean isAopProxy(Object object) {
        return AopUtils.isAopProxy(object);
    }

    /**
     * Find target class class.
     *
     * @param proxy the proxy
     * @return the class
     * @throws Exception the exception
     */
    public static Class<?> findTargetClass(Object proxy)  {
       try {
           if (AopUtils.isAopProxy(proxy)) {
               AdvisedSupport advised = getAdvisedSupport(proxy);
               if (AopUtils.isJdkDynamicProxy(proxy)) {
                   TargetSource targetSource = advised.getTargetSource();
                   return targetSource instanceof EmptyTargetSource ? getFirstInterfaceByAdvised(advised)
                           : targetSource.getTargetClass();
               }
               Object target = advised.getTargetSource().getTarget();
               return findTargetClass(target);
           } else {
               return proxy == null ? null : proxy.getClass();
           }
       }catch (Exception e){
               e.printStackTrace();
               throw  new RuntimeException("findTargetClass  出现异常");
       }
    }

    public static Class<?> findTargetInsterface(Object proxy)  {
        Class<?> targetClass = findTargetClass(proxy);
        Class<?>[] interfaces = targetClass.getInterfaces();
        return  interfaces!=null&&interfaces.length>0? interfaces[0]:null;
    }

    public static Class<?>[] findInterfaces(Object proxy) throws Exception {
        if (AopUtils.isJdkDynamicProxy(proxy)) {
            AdvisedSupport advised = getAdvisedSupport(proxy);
            return getInterfacesByAdvised(advised);
        } else {
            return new Class<?>[]{};
        }
    }

    private static Class<?>[] getInterfacesByAdvised(AdvisedSupport advised) {
        Class<?>[] interfaces = advised.getProxiedInterfaces();
        if (interfaces.length > 0) {
            return interfaces;
        } else {
            throw new IllegalStateException("Find the jdk dynamic proxy class that does not implement the interface");
        }
    }

    private static Class<?> getFirstInterfaceByAdvised(AdvisedSupport advised) {
        Class<?>[] interfaces = advised.getProxiedInterfaces();
        if (interfaces.length > 0) {
            return interfaces[0];
        } else {
            throw new IllegalStateException("Find the jdk dynamic proxy class that does not implement the interface");
        }
    }

    /**
     * Gets advised support.
     *
     * @param proxy the proxy
     * @return the advised support
     * @throws Exception the exception
     */
    public static AdvisedSupport getAdvisedSupport(Object proxy) {
        try {
            Field h;
            if (AopUtils.isJdkDynamicProxy(proxy)) {
                h = proxy.getClass().getSuperclass().getDeclaredField("h");
            } else {
                h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
            }
            h.setAccessible(true);
            Object dynamicAdvisedInterceptor = h.get(proxy);
            Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
            advised.setAccessible(true);
            return (AdvisedSupport) advised.get(dynamicAdvisedInterceptor);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("getAdvisedSupport:获取增强器出现异常");
        }
    }

    /**
     * Is proxy boolean.
     *
     * @param bean the bean
     * @return the boolean
     */
    public static boolean isProxy(Object bean) {
        if (bean == null) {
            return false;
        }
        //check dubbo proxy ?
//        return DubboUtil.isDubboProxyName(bean.getClass().getName()) || (Proxy.class.isAssignableFrom(bean.getClass())
//                || AopUtils.isAopProxy(bean));
        return  (Proxy.class.isAssignableFrom(bean.getClass())
                || AopUtils.isAopProxy(bean));
    }

    /**
     * Get the target class , get the interface of its agent if it is a Proxy
     *
     * @param proxy the proxy
     * @return target interface
     * @throws Exception the exception
     */
    public static Class<?> getTargetInterface(Object proxy) throws Exception {
        if (proxy == null) {
            throw new IllegalArgumentException("proxy can not be null");
        }

        //jdk proxy
        if (Proxy.class.isAssignableFrom(proxy.getClass())) {
            Proxy p = (Proxy) proxy;
            return p.getClass().getInterfaces()[0];
        }

        return getTargetClass(proxy);
    }

    /**
     * Get the class type of the proxy target object, if hadn't a target object, return the interface of the proxy
     *
     * @param proxy
     * @return
     * @throws Exception
     */
    protected static Class<?> getTargetClass(Object proxy) throws Exception {
        if (proxy == null) {
            throw new IllegalArgumentException("proxy can not be null");
        }
        //not proxy
        if (!AopUtils.isAopProxy(proxy)) {
            return proxy.getClass();
        }
        AdvisedSupport advisedSupport = getAdvisedSupport(proxy);
        Object target = advisedSupport.getTargetSource().getTarget();
        /*
         * the Proxy of sofa:reference has no target
         */
        if (target == null) {
            if (CollectionUtils.isNotEmpty(advisedSupport.getProxiedInterfaces())) {
                return advisedSupport.getProxiedInterfaces()[0];
            } else {
                return proxy.getClass();
            }
        } else {
            return getTargetClass(target);
        }
    }

    /**
     * get the all interfaces of bean, if the bean is null, then return empty array
     *
     * @param bean
     * @return
     */
    public static Class<?>[] getAllInterfaces(Object bean) {
        Set<Class<?>> interfaces = new HashSet<>();
        if (bean != null) {
            Class<?> clazz = bean.getClass();
            while (!Object.class.getName().equalsIgnoreCase(clazz.getName())) {
                Class<?>[] clazzInterfaces = clazz.getInterfaces();
                interfaces.addAll(Arrays.asList(clazzInterfaces));
                clazz = clazz.getSuperclass();
            }
        }
        return interfaces.toArray(new Class[0]);
    }

}

