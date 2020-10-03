package com.jin.serializable;

import com.jin.dto.RPCDto;
import com.jin.utils.HessianUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * creat by Jin 2020/9/3 04:23
 *
 * @Description:
 */
public class RPCDtoEncoder extends MessageToByteEncoder<RPCDto> {
    AtomicInteger integer=new AtomicInteger(0);
    @Override
    protected void encode(ChannelHandlerContext ctx, RPCDto msg, ByteBuf out) throws Exception {
        byte[] serialize=null;
       try {
           serialize = HessianUtils.serialize(msg);
       }catch (Exception e){
             e.printStackTrace();
       }
        out.writeBytes(serialize);
        ctx.flush();
    }
}
