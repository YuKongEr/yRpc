package cn.yukonga.yrpc.client;

import cn.yukonga.yrpc.client.annotation.RemoteReferenceAnnotationBeanPostProcessor;
import cn.yukonga.yrpc.core.config.ZookeeperConfig;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * @author : yukong
 */
@Component
public class ServiceRecovery {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ZooKeeper zooKeeper;

    private CountDownLatch countDownLatch = new CountDownLatch(1);

    @Autowired
    private ZookeeperConfig registerClientConfig;

    /**
     * 连接zk
     * @throws IOException 抛出IOException
     * @throws InterruptedException 抛出InterruptedException
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
     * @return 服务名与地址map
     * @throws IOException 抛出IOException
     * @throws InterruptedException 抛出InterruptedException
     */
    public Map<String, String> recoverServices() throws IOException, InterruptedException {
        Map<String, String> serviceAddressMap = new ConcurrentHashMap<>(16);
        connect();
        Set<String> serviceNames =   RemoteReferenceAnnotationBeanPostProcessor.needRecoveryServiceNames;
        String rootPath = registerClientConfig.getRootPath();
        serviceNames.forEach(serviceName -> {
            recoverService(serviceAddressMap, serviceName);
        });
        return serviceAddressMap;
    }


    /**
     * 发现指定服务和它的地址
     */
    public void recoverService( Map<String, String> serviceAddressMap, String serviceName){

        String rootPath = registerClientConfig.getRootPath();

            String servicePath = rootPath + "/" + serviceName;
            // 节点存在
            try {
                if(zooKeeper.exists(servicePath, false) != null) {
                    List<String> children = zooKeeper.getChildren(servicePath, false);
                    if (!CollectionUtils.isEmpty(children)){
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
    }

}
