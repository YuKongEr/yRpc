package com.yukong.yrpc.client;

import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;

/**
 * @author: yukong
 * @date: 2019/1/3 14:24
 */
public class RpcClientChannelHolder {

    private Channel channel;
    private EventLoopGroup eventLoopGroup;

    public RpcClientChannelHolder() {
    }

    public RpcClientChannelHolder(Channel channel, EventLoopGroup eventLoopGroup){
        this.channel = channel;
        this.eventLoopGroup = eventLoopGroup;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public EventLoopGroup getEventLoopGroup() {
        return eventLoopGroup;
    }

    public void setEventLoopGroup(EventLoopGroup eventLoopGroup) {
        this.eventLoopGroup = eventLoopGroup;
    }


}
