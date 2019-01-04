package com.yukong.yrpc.client;

import com.yukong.yrpc.core.annotation.RemoteRefrence;
import com.yukong.yrpc.core.config.RegisterClientConfig;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * @author: yukong
 * @date: 2019/1/2 17:50
 */
@Component
public class ServiceRecovery {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ZooKeeper zooKeeper;

    private CountDownLatch countDownLatch = new CountDownLatch(1);

    @Autowired
    private RegisterClientConfig registerClientConfig;

    /**
     * 连接zk
     * @throws IOException
     * @throws InterruptedException
     */
    private void connect() throws IOException, InterruptedException {

        String zookeeperAddress = registerClientConfig.getAddress();
        zooKeeper = new ZooKeeper(zookeeperAddress, registerClientConfig.getSessionTimeOut(), new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                if (event.getState().equals(Event.KeeperState.SyncConnected)){
                    countDownLatch.countDown();
                }
            }
        });
        countDownLatch.await();
    }

    /**
     * 发现服务和它的地址
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public Map<String, String> recoverServices() throws IOException, InterruptedException {
        Map<String, String> serviceAddressMap = new ConcurrentHashMap<>(16);
        connect();
        Reflections reflections = new Reflections(registerClientConfig.getRemoteApiPackage());
        Set<Class<?>> typesWithAnnotated = reflections.getTypesAnnotatedWith(RemoteRefrence.class);
        Set<String> serviceNames = typesWithAnnotated.stream().map(cl -> cl.getName()).collect(Collectors.toSet());
        String rootPath = registerClientConfig.getRootPath();
        serviceNames.forEach( serviceName -> {
            String servicePath = rootPath + "/" + serviceName;
            // 节点存在
            try {
                if(zooKeeper.exists(servicePath, false) != null) {
                    List<String> children = zooKeeper.getChildren(servicePath, false);
                    if (!StringUtils.isEmpty(children)){
                        //地址多于一个取第一个，可以扩展做负载均衡
                        byte[] bytes = zooKeeper.getData(servicePath + "/" + children.get(0), false, null);
                        String address = new String(bytes);
                        serviceAddressMap.put(serviceName, address);
                        if(logger.isDebugEnabled()) {
                            logger.debug("recover service {}", serviceName);
                        }
                    } else {
                        logger.info("zookeeper not provider service {}, service path {}", serviceName, servicePath);
                    }
                }
            } catch (KeeperException | InterruptedException e) {
               logger.error("error: {}", e);
            }
        });
        return serviceAddressMap;
    }

}
