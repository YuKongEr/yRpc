package com.yukong.yrpc.core.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: yukong
 * @date: 2019/1/2 14:49
 */
@Component
@Target(value = {ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RemoteReference {



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

    String interfaceName() default "";

    Class interfaceClassName() default void.class;

}
