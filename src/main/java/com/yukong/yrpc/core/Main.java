package com.yukong.yrpc.core;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: yukong
 * @date: 2019/1/3 16:13
 */
public class Main {

    public static void main(String[] args) {
        String hostPort = "193.112.100.103:2181";
        String zpath = "/";
        List<String> zooChildren = new ArrayList<String>();
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(hostPort, 2000, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (zk != null) {
            try {
                zooChildren = zk.getChildren(zpath, false);
                System.out.println("Znodes of '/': ");
                for (String child: zooChildren) {
                    //print the children
                    System.out.println(child);
                }
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
