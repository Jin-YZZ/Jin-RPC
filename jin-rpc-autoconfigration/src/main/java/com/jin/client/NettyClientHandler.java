package com.jin.client;

import com.jin.core.chain.FilterChainManager;
import com.jin.dto.RPCDto;
import com.jin.utils.ApplicationContextUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.SneakyThrows;


/**
 * creat by Jin 2020/9/2 09:08
 *
 * @Description:
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<RPCDto> {

    @SneakyThrows
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RPCDto msg) throws Exception {
        getfilterChainManager().handleClientReceive(msg);
    }

    //处理异常, 一般是需要关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }


    public FilterChainManager getfilterChainManager() {
        return ApplicationContextUtils.getBeanByClazz(FilterChainManager.class);
    }

}
