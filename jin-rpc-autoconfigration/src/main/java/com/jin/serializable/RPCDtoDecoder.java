package com.jin.serializable;

import com.jin.dto.RPCDto;
import com.jin.utils.HessianUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import java.util.List;

/**
 * creat by Jin 2020/9/3 04:26
 *
 * @Description:
 */
public class RPCDtoDecoder extends ByteToMessageDecoder{
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        Object obj = HessianUtils.deserialize(HessianUtils.read(in));
        out.add(obj);
    }
}
