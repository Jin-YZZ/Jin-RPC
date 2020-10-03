package com.jin.error;

/**
 * creat by Jin 2020/9/14 14:57
 *
 * @Description:
 */
public class RpcException extends RuntimeException {

    public RpcException(String message) {
        super(message);
    }

    protected RpcException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public static void throwRpcException(String error) {
        throw new RuntimeException(error);
    }

    public static void throwRpcException(String message, Throwable cause) {
        throw new RpcException(message,cause,true,true);
    }
}
