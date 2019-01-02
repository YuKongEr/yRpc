package com.yukong.yrpc.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author: yukong
 * @date: 2019/1/2 14:13
 */
@Component
public class RegisterClientConfig {


    @Value("${yRpc.zookeeper.rootPath}")
    private String rootPath;

    @Value("${yRpc.zookeeper.address}")
    private String address;

    @Value("${yRpc.zookeeper.sessionTimeOut}")
    private Integer sessionTimeOut;

    @Value("${yRpc.netty.server}")
    private String serverHost;

    @Value("${yRpc.netty.port}")
    private int serverPort;

    @Value("${yRpc.remote.api.package}")
    private String remoteApiPackage;

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

    public String getServerHost() {
        return serverHost;
    }

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public String getRemoteApiPackage() {
        return remoteApiPackage;
    }

    public void setRemoteApiPackage(String remoteApiPackage) {
        this.remoteApiPackage = remoteApiPackage;
    }
}
