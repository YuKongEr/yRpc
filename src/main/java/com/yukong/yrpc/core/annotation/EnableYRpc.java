package com.yukong.yrpc.core.annotation;

import com.yukong.yrpc.client.annotation.EnableYRpcClient;
import com.yukong.yrpc.client.annotation.YRpcClientRegistrar;
import com.yukong.yrpc.server.annotation.EnableYRpcServer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

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
