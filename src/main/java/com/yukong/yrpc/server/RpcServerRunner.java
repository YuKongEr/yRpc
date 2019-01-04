package com.yukong.yrpc.server;

import com.yukong.yrpc.core.config.RegisterServerConfig;
import com.yukong.yrpc.core.protocol.Parse;
import com.yukong.yrpc.core.protocol.RpcServerDecoder;
import com.yukong.yrpc.core.protocol.RpcServerEncoder;
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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: yukong
 * @date: 2018/12/29 17:12
 */
@Component
public class RpcServerRunner implements InitializingBean, ApplicationContextAware {


    private final Logger logger = LoggerFactory.getLogger(RpcServerRunner.class);

    private EventLoopGroup bossGroup = null;
    private EventLoopGroup workerGroup = null;

    private ApplicationContext applicationContext;

    @Autowired
    private Parse protostuffParse;

    @Autowired
    private RegisterServerConfig registerServerConfig;




    public void stop() {
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
    }


    public void start() throws Exception {
        ServiceRegistry serviceRegistry = new ServiceRegistry(registerServerConfig);
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
                                        .addLast(new RpcServerDecoder(protostuffParse))
                                        .addLast(new RpcServerEncoder(protostuffParse))
                                        .addLast(new RpcServerHandler(applicationContext));
                            }
                        })
                        .option(ChannelOption.SO_BACKLOG, 128)
                        .childOption(ChannelOption.SO_KEEPALIVE, true);

                int port = registerServerConfig.getServerPort();
                try {
                    ChannelFuture future = bootstrap.bind(registerServerConfig.getServerHost(), port).sync();
                    logger.info("Server started on port {}", port);
                    future.channel().closeFuture().sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                start();
            } catch (Exception e) {
               logger.error("error: {}", e);
            }
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
