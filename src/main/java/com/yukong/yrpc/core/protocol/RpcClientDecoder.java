package com.yukong.yrpc.core.protocol;

import com.yukong.yrpc.core.model.RpcResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author: yukong
 * @date: 2018/12/29 14:06
 */
public class RpcClientDecoder extends ByteToMessageDecoder {

    private Parse parse;

    public RpcClientDecoder(Parse parse) {
        this.parse = parse;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> list) throws Exception {

        if (in.readableBytes() <= 4) {
            return;
        }
        int length = in.readInt();
        in.markReaderIndex();

        if(in.readableBytes() < length) {
            in.resetReaderIndex();
        }

        byte[] dst = new byte[length];
        in.readBytes(dst);
        RpcResponse rpcRequest = parse.deSerialize(dst, RpcResponse.class);
        list.add(rpcRequest);
    }
}
