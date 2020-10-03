package com.jin.core.chain;
/**
 * creat by Jin 2020/9/5 21:47
 *
 * @Description:
 */
public interface FiterJoinpoin {

    void processClientSend() throws Throwable;

    Object processServerReceive() throws Throwable;

    Object processClientReceive() throws Throwable;

}
