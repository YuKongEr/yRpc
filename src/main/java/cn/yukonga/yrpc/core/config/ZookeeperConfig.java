package cn.yukonga.yrpc.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: yukong
 * @date: 2019/1/2 14:13
 */
@Component
@ConfigurationProperties(prefix = "yrpc.zookeeper")
public class ZookeeperConfig {


    private String rootPath = "/rpc";

    private String address;

    private Integer sessionTimeOut;


    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getSessionTimeOut() {
        return sessionTimeOut;
    }

    public void setSessionTimeOut(Integer sessionTimeOut) {
        this.sessionTimeOut = sessionTimeOut;
    }

}
