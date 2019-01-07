package com.yukong.yrpc.client;

import com.yukong.yrpc.core.model.RpcRequest;
import com.yukong.yrpc.core.protocol.Parse;
import com.yukong.yrpc.core.protocol.RpcClientDecoder;
import com.yukong.yrpc.core.protocol.RpcClientEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author: yukong
 * @date: 2019/1/3 10:48
 */
@Component
public class RpcClientRunner implements InitializingBean, DisposableBean {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 服务名称与服务在zk中的path
     */
    private Map<String, String> serviceAddressMap;

    /**
     * 服务名称与netty的channel的绑定
     */
    private Map<String, RpcClientChannelHolder> addressChannelMap = new ConcurrentHashMap<>();
    ;


    @Autowired
    private RpcClientRequestPool rpcClientRequestPool;

    @Autowired
    private Parse protostuffParse;

    @Autowired
    private RpcClientHandler clientHandler;

    @Autowired
    private ServiceRecovery serviceRecovery;

    /**
     * 发送请求
     *
     * @param rpcRequest
     */
    public void send(RpcRequest rpcRequest) {
        String serviceName = rpcRequest.getClassName();
        String address = serviceAddressMap.get(serviceName);
        boolean isProvided = false;
        if (address == null) {
            serviceRecovery.recoverService(serviceAddressMap, serviceName);
        }
        address = serviceAddressMap.get(serviceName);
        if (address != null) {
            RpcClientChannelHolder channelHold = addressChannelMap.get(address);
            if (channelHold == null) {
                channelHold = createConnection(address);
            }
            Channel channel = channelHold.getChannel();
            rpcClientRequestPool.addRequest(rpcRequest.getRequestId(), channel.eventLoop());
            channel.writeAndFlush(rpcRequest);
            isProvided = true;
        }
        if (!isProvided) {
            logger.error("Service Server Not Provided! {}", serviceName);
        }
    }

    public void createConnection() {
        try {
            this.serviceAddressMap = serviceRecovery.recoverServices();
        } catch (IOException | InterruptedException e) {
            logger.error("error: {}", e);
        }
        Set<String> addresses = serviceAddressMap.values().stream().distinct().collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(addresses)) {
            return;
        }
        for (String address : addresses) {
            createConnection(address);
        }
    }

    public RpcClientChannelHolder createConnection(String address) {
        String host = null;
        Integer port = null;
        try {
            String[] split = address.split(":");
            host = split[0];
            port = Integer.valueOf(split[1]);
        } catch (IndexOutOfBoundsException e) {
            logger.error("address [{}] invalid!", address);
            return null;
        }
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        bootstrap.channel(NioSocketChannel.class)
                .group(eventLoopGroup)
                .remoteAddress(host, port)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new RpcClientEncoder(protostuffParse));
                        pipeline.addLast(new RpcClientDecoder(protostuffParse));
                        pipeline.addLast(clientHandler);
                    }
                });
        Channel channel = bootstrap.connect().channel();
        RpcClientChannelHolder channelHold = new RpcClientChannelHolder(channel, eventLoopGroup);
        addressChannelMap.put(address, channelHold);
        return channelHold;
    }

    /**
     * 安全关闭
     *
     * @throws Exception
     */
    @Override
    public void destroy() throws Exception {
        if (addressChannelMap != null) {
            Collection<RpcClientChannelHolder> channelHolds = addressChannelMap.values();
            if (!CollectionUtils.isEmpty(channelHolds)) {
                channelHolds.forEach(channelHold -> {
                    channelHold.getChannel().closeFuture();
                    channelHold.getEventLoopGroup().shutdownGracefully();
                });
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        createConnection();
    }
}
