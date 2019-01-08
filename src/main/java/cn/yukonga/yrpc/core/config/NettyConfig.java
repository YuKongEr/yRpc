package cn.yukonga.yrpc.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: yukong
 * @date: 2019/1/8 16:54
 */
@Component
@ConfigurationProperties(prefix = "yrpc.netty")
public class NettyConfig {

    private String host;

    private Integer port;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
