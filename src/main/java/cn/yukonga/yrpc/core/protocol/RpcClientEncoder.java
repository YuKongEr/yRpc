package cn.yukonga.yrpc.core.protocol;

import cn.yukonga.yrpc.core.model.RpcRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author : yukong
  */
public class RpcClientEncoder extends MessageToByteEncoder<RpcRequest> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private Parse parse;

    public RpcClientEncoder(Parse parse) {
        this.parse = parse;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RpcRequest rpcResponse, ByteBuf out) throws Exception {
        logger.info("server response client request {}", rpcResponse);
        byte[] data = parse.serialize(rpcResponse);
        out.writeInt(data.length);
        out.writeBytes(data);
    }


}