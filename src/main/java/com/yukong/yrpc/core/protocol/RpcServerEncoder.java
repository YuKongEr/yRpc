package com.yukong.yrpc.core.protocol;

import com.yukong.yrpc.core.model.RpcResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author: yukong
 * @date: 2018/12/29 16:07
 */
public class RpcServerEncoder extends MessageToByteEncoder<RpcResponse> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private Parse parse;

    public RpcServerEncoder(Parse parse) {
        this.parse = parse;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RpcResponse rpcResponse, ByteBuf out) throws Exception {
        logger.info("server response client request {}", rpcResponse);
        byte[] data = parse.serialize(rpcResponse);
        out.writeInt(data.length);
        out.writeBytes(data);
    }


}