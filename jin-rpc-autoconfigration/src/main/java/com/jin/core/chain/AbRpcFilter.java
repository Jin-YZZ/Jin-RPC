package com.jin.core.chain;

import com.jin.dto.RPCDto;

/**
 * creat by Jin 2020/9/6 09:18
 *
 * @Description:
 */
public abstract class AbRpcFilter implements RpcFilter {
    @Override
    public void invokeClinentSend(RPCDto dto, FiterJoinpoin fiterJoinpoin) throws Throwable {
        fiterJoinpoin.processClientSend();
    }

    @Override
    public Object invokeServerReceive(RPCDto dto, FiterJoinpoin fiterJoinpoin) throws Throwable {
        return fiterJoinpoin.processServerReceive();
    }


    @Override
    public Object invokeClinentReceive(RPCDto dto, FiterJoinpoin fiterJoinpoin) throws Throwable {
        return fiterJoinpoin.processClientReceive();
    }


}
