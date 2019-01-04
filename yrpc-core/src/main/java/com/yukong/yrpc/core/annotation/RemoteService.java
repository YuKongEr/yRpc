package com.yukong.yrpc.core.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author: yukong
 * @date: 2018/12/29 11:22
 * 远程接口注解
 *
 */
@Component
@Target(value = ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RemoteService {

    /**
     * 实现的接口类型
     * @return
     */
    Class<?> value();

    /**
     * 是否子类代理
     * 是： 使用cglib
     * 否：使用jdk
     * @return
     */
    boolean isProxyTargetClass() default false;

    /**
     * 使用的协议类型
     * @return
     */
    String protocol() default "";

}
