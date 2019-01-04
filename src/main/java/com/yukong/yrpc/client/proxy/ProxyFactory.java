package com.yukong.yrpc.client.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;

/**
 * @author: yukong
 * @date: 2019/1/2 17:18
 */
@Component
public class ProxyFactory  {

    @Autowired
    private CglibProxyInvoker cglibProxyInvoker;

    @Autowired
    private JdkProxyInvoker jdkProxyInvoker;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public <T> T createInstance(Class<T> interfaceClass){
        return createInstance(interfaceClass, false);
    }

    @SuppressWarnings("unchecked")
    public <T> T createInstance(Class<T> cls, boolean isTargetClass){
        if (isTargetClass){
            logger.info("use cglib : " + cls.getSimpleName());
            Enhancer enhancer = new Enhancer();
            enhancer.setCallback(cglibProxyInvoker);
            enhancer.setSuperclass(cls);
            return (T) enhancer.create();
        }else {
            logger.info("use jdk dynamic proxy : " + cls.getSimpleName());
            return (T) Proxy.newProxyInstance(cls.getClassLoader(),
                    new Class[]{cls}, jdkProxyInvoker);
        }
    }

}
