package com.jin.server;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.jin.core.chain.FilterChainManager;
import com.jin.dto.RPCDto;
import com.jin.utils.ApplicationContextUtils;
import com.jin.utils.HessianUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import io.seata.core.context.RootContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * creat by Jin 2020/9/2 21:15
 *
 * @Description:
 */
@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<RPCDto> {

    FilterChainManager filterChainManager;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RPCDto msg) throws Exception {
        log.info("接收到客户端消息消息：{}", msg);
        ctx.channel().eventLoop().execute(() -> {
            try {
                msg.setReturnValue(getfilterChainManager().handleServerReceive(msg));
                ctx.writeAndFlush(msg);
            } catch (Throwable e) {
                msg.setOK(false);
                msg.setErrorMessage(msg.getId()+"调用失败！失败原因: "+e.getMessage());
                ctx.writeAndFlush(msg);
            }
        });
    }

    //处理异常, 一般是需要关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }


    public FilterChainManager getfilterChainManager() {
        return filterChainManager != null ? filterChainManager : ApplicationContextUtils.getBeanByClazz(FilterChainManager.class);
    }


}