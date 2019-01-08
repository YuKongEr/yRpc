package cn.yukonga.yrpc.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: yukong
 * @date: 2018/12/29 11:30
 */
@Component
@ConfigurationProperties(prefix = "yrpc")
public class RegisterServerConfig {

   private ZookeeperConfig zookeeper;

   private NettyConfig netty;


    public ZookeeperConfig getZookeeper() {
        return zookeeper;
    }

    public void setZookeeper(ZookeeperConfig zookeeper) {
        this.zookeeper = zookeeper;
    }

    public NettyConfig getNetty() {
        return netty;
    }

    public void setNetty(NettyConfig netty) {
        this.netty = netty;
    }
}
