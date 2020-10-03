package com.jin.dto;

import io.netty.channel.Channel;

/**
 * creat by Jin 2020/9/4 08:18
 *
 * @Description:
 */
public class ChannelAndDto {
   public Channel channel;
   public  RPCDto dto;

    public ChannelAndDto(Channel channel, RPCDto dto) {
        this.channel = channel;
        this.dto = dto;
    }

    public RPCDto getDto() {
        return dto;
    }

    public void setDto(RPCDto dto) {
        this.dto = dto;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
