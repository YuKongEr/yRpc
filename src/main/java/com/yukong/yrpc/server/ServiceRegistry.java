package com.yukong.yrpc.server;

import com.yukong.yrpc.core.annotation.RemoteService;
import com.yukong.yrpc.core.config.RegisterServerConfig;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @author: yukong
 * @date: 2018/12/29 11:39
 */
@Component
public class ServiceRegistry implements ApplicationContextAware {

    private Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private RegisterServerConfig registerServerConfig;

    private ZooKeeper zookeeper;

    public ServiceRegistry(RegisterServerConfig registerServerConfig) {
        this.registerServerConfig = registerServerConfig;
    }

    private CountDownLatch countDownLatch = new CountDownLatch(1);

    public void init(ApplicationContext applicationContext) {
        //连接zookeeper
        try {
            connect();
            countDownLatch.await();
        } catch (IOException | InterruptedException e) {
            logger.error("", e);
        }
        //创建根节点路径
        createRootPath();
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(RemoteService.class);
        if (!CollectionUtils.isEmpty(beansWithAnnotation)){
            beansWithAnnotation.values().forEach(serviceBean -> {
                String serviceName = serviceBean.getClass().getAnnotation(RemoteService.class).value().getName();
                logger.info("register @RemoteService : " + serviceName);
                createServiceAddressNode(serviceName);
            });
        }
    }

    /**
     * 连接zk服务器
     * @throws IOException
     */
    private void connect() throws IOException, InterruptedException {
        zookeeper = new ZooKeeper(registerServerConfig.getAddress(), registerServerConfig.getSessionTimeOut(), new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                if (watchedEvent.getState().equals(Watcher.Event.KeeperState.SyncConnected)){
                    countDownLatch.countDown();
                }
            }
        });
        countDownLatch.await();
    }

    /**
     * 创建根节点
     */
    private void createRootPath() {
        try{
            // 获取根节点
            Stat stat = zookeeper.exists(rootPath(), false);
            // 如果根节点不存在 则创建
            if (stat == null) {
                zookeeper.create(rootPath(), new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (InterruptedException | KeeperException e) {
            logger.error("",e);
        }
    }

    /**
     * 创建服务节点
     * @param serviceName
     */
    private void createServiceNode(String serviceName) {
        try {
            String servicePath = rootPath() + "/" + serviceName;
            Stat stat = zookeeper.exists(servicePath, false);
            if (stat == null){
                zookeeper.create(servicePath, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (KeeperException | InterruptedException e) {
            logger.error("", e);
        }
    }

    /**
     * 创建服务接口地址节点
     * @param serviceName 服务接口名
     */
    private void createServiceAddressNode(String serviceName) {
        createServiceNode(serviceName);
        String serverHost = registerServerConfig.getServerHost();
        int serverPort = registerServerConfig.getServerPort();
        String serverAddress = serverHost + ":" + serverPort;
        String serviceAddressPath = rootPath() + "/" + serviceName + "/" + serverAddress;
        try {
            zookeeper.create(serviceAddressPath, serverAddress.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        } catch (KeeperException | InterruptedException e) {
            logger.error("", e);
        }
    }


    private String rootPath() {
        return registerServerConfig.getRootPath();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
       init(applicationContext);
    }
}
