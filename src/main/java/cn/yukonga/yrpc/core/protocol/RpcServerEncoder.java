package cn.yukonga.yrpc.core.protocol;

import cn.yukonga.yrpc.core.model.RpcResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author : yukong
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