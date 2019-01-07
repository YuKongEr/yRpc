package com.yukong.yrpc.client;

import com.yukong.yrpc.client.proxy.ProxyFactory;
import com.yukong.yrpc.core.annotation.RemoteReference;
import com.yukong.yrpc.core.config.RegisterClientConfig;
import org.reflections.Reflections;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.util.CollectionUtils;

import java.util.Set;

/**
 * @author: yukong
 * @date: 2019/1/2 14:10
 */
@Configuration
@Order
public class RpcClientConfig implements ApplicationContextAware , InitializingBean {

    private ApplicationContext applicationContext;

    @Autowired
    private RegisterClientConfig registerClientConfig;

    @Autowired
    private ProxyFactory proxyFactory;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        Reflections reflections = new Reflections(registerClientConfig.getRemoteApiPackage());
        Set<Class<?>> typesWithAnnotated = reflections.getTypesAnnotatedWith(RemoteReference.class);

        if (!CollectionUtils.isEmpty(typesWithAnnotated)){
            DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
            typesWithAnnotated.forEach(cls -> {
                RemoteReference annotation = cls.getAnnotation(RemoteReference.class);
                if (annotation.isProxyTargetClass()){
                    beanFactory.registerSingleton(cls.getName(), proxyFactory.createInstance(cls, true));
                }else {
                    beanFactory.registerSingleton(cls.getName(), proxyFactory.createInstance(cls, false));
                }
            });
        }
    }

}
