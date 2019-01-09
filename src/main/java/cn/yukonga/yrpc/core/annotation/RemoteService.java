package cn.yukonga.yrpc.core.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : yukong
  * 远程接口注解
 *
 */
@Component
@Target(value = ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RemoteService {

    /**
     * 实现的接口类型
     * @return 接口类型
     */
    Class<?> value();

    /**
     * 是否子类代理
     * 是： 使用cglib
     * 否：使用jdk
     * @return 子类代理
     */
    boolean isProxyTargetClass() default false;

    /**
     * 使用的协议类型
     * @return 协议类型
     */
    String protocol() default "";

}
