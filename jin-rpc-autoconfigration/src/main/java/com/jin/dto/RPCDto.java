package com.jin.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * creat by Jin 2020/8/30 19:47
 *
 * @Description:
 */
@Data
public class RPCDto implements Serializable {

    String Id;
    String methodName;
    String clazzName;
    Class<?>[] parameterTypes;
    Object[] args;
    Class<?> returnType;
    Object returnValue;
    Class<?> beanClass;
    String xid;

    boolean OK=true;
    String errorMessage;

}
