package cn.yukonga.yrpc.core.annotation;

import cn.yukonga.yrpc.client.annotation.EnableYRpcClient;
import cn.yukonga.yrpc.server.annotation.EnableYRpcServer;

import java.lang.annotation.*;

/**
 * @author: yukong
 * @date: 2019/1/8 14:37
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableYRpcClient
@EnableYRpcServer
public @interface EnableYRpc {
}
