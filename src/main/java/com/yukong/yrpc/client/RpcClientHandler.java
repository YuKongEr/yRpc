package com.yukong.yrpc.client;

import com.yukong.yrpc.core.model.RpcResponse;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: yukong
 * @date: 2019/1/3 14:01
 */
@Component
@ChannelHandler.Sharable
public class RpcClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RpcClientRequestPool rpcClientRequestPool;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse response) throws Exception {
        logger.info("response from server : " + response);
        rpcClientRequestPool.notifyRequest(response.getRequestId(), response);
    }
}
