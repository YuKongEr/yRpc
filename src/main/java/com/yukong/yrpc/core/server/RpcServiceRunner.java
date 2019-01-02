package com.yukong.yrpc.core.server;

import com.yukong.yrpc.core.config.RegisterServerConfig;
import com.yukong.yrpc.core.protocol.Parse;
import com.yukong.yrpc.core.protocol.RpcDecoder;
import com.yukong.yrpc.core.protocol.RpcEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author: yukong
 * @date: 2018/12/29 17:12
 */
@Component
public class RpcServiceRunner implements ApplicationContextAware, InitializingBean {

    private ApplicationContext applicationContext;

    private  final Logger logger = LoggerFactory.getLogger(RpcServiceRunner.class);

    private EventLoopGroup bossGroup = null;
    private EventLoopGroup workerGroup = null;

    @Autowired
    private ServiceRegistry serviceRegistry;

    @Autowired
    private Parse protostuffParse;

    @Autowired
    private RegisterServerConfig registerServerConfig;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        start();
    }

    public void stop() {
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
    }


    public void start() throws Exception {
        if (bossGroup == null && workerGroup == null) {
            bossGroup = new NioEventLoopGroup();
            workerGroup = new NioEventLoopGroup();
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline()
                                    .addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0))
                                    .addLast(new RpcDecoder(protostuffParse))
                                    .addLast(new RpcEncoder(protostuffParse))
                                    .addLast(new RpcHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            int port = registerServerConfig.getServerPort();
            ChannelFuture future = bootstrap.bind(registerServerConfig.getServerHost(), port).sync();
            logger.info("Server started on port {}", port);
            if (serviceRegistry != null) {
                serviceRegistry.init(applicationContext);
            }
            future.channel().closeFuture().sync();
        }
    }
}
