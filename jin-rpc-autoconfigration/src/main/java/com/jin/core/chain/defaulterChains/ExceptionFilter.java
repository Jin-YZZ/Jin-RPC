package com.jin.core.chain.defaulterChains;

import com.jin.core.chain.AbRpcFilter;
import com.jin.core.chain.FiterJoinpoin;
import com.jin.dto.RPCDto;
import io.netty.channel.ChannelHandlerContext;

/**
 * creat by Jin 2020/9/14 17:23
 *
 * @Description:
 */
public class ExceptionFilter extends AbRpcFilter {


    @Override
    public void invokeClinentSend(RPCDto dto, FiterJoinpoin fiterJoinpoin) {
        try {
            super.invokeClinentSend(dto, fiterJoinpoin);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    @Override
    public Object invokeServerReceive(RPCDto dto, FiterJoinpoin fiterJoinpoin) {
        try {
            return super.invokeServerReceive(dto, fiterJoinpoin);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    @Override
    public Object invokeClinentReceive(RPCDto dto, FiterJoinpoin fiterJoinpoin) {
        try {
            return super.invokeClinentReceive(dto, fiterJoinpoin);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    @Override
    public int getOrder() {
        return -1000;
    }
}
