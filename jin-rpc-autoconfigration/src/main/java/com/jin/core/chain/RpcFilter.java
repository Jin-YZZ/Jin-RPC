package com.jin.core.chain;

import com.jin.dto.RPCDto;
import org.springframework.core.Ordered;

/**
 * creat by Jin 2020/9/5 21:09
 *
 * @Description:
 */
public interface RpcFilter  extends Ordered {

   void invokeClinentSend(RPCDto dto, FiterJoinpoin fiterJoinpoin) throws Throwable;

   Object invokeServerReceive(RPCDto dto,FiterJoinpoin fiterJoinpoin) throws Throwable;

   Object invokeClinentReceive(RPCDto dto,FiterJoinpoin fiterJoinpoin) throws Throwable;
}
