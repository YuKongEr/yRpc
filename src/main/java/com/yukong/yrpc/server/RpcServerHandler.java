package com.yukong.yrpc.server;


import com.yukong.yrpc.core.annotation.RemoteService;
import com.yukong.yrpc.core.model.RpcRequest;
import com.yukong.yrpc.core.model.RpcResponse;
import com.yukong.yrpc.server.proxy.MethodProxy;
import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

/**
 * @author: yukong
 * @date: 2018/12/29 16:39
 */
@ChannelHandler.Sharable
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private ApplicationContext applicationContext;

    private final Logger logger = LoggerFactory.getLogger(RpcServerHandler.class);

    public RpcServerHandler(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest rpcRequest) {
        logger.info("receive client request {}", rpcRequest);
        RpcResponse response = new RpcResponse();
        response.setRequestId(rpcRequest.getRequestId());
        try {
            Object result = handler(rpcRequest);
            // 业务处理
            response.setResult(result);
        } catch (InvocationTargetException | ClassNotFoundException  | NoSuchMethodException | IllegalAccessException e) {
           logger.error("error:{}", e);
           response.setException(e);
        }
        channelHandlerContext.writeAndFlush(response).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                logger.debug("Send response for request " + rpcRequest.getRequestId());
            }
        });
    }

    private Object handler(RpcRequest rpcRequest) throws InvocationTargetException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException {
        String className = rpcRequest.getClassName();
        Object targetBean = applicationContext.getBean(Class.forName(className));
        Boolean isProxyTargetClass = targetBean.getClass().getAnnotation(RemoteService.class).isProxyTargetClass();
        MethodProxy methodProxy = MethodProxy.getMethodProxy(isProxyTargetClass);
        return methodProxy.invoke(rpcRequest,targetBean);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("server caught exception", cause);
        ctx.close();
    }


}
