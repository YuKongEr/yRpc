package cn.yukonga.yrpc.client.beans;

import cn.yukonga.yrpc.client.proxy.ProxyFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;


/**
 * @author: yukong
 * @date: 2019/1/7 11:44
 */
public class RemoteReferenceBean<T> implements DisposableBean {

    private ApplicationContext applicationContext;

    private T object;

    private Class<T> cls;

    private Boolean isTargetClass;



    public T getObject() {

        if(object == null) {
            init();
        }
        return object;
    }

    private void init() {
        ProxyFactory proxyFactory = applicationContext.getBean(ProxyFactory.class);
        object = proxyFactory.createInstance(cls, isTargetClass);
    }

    public RemoteReferenceBean(Class<T> cls, boolean isTargetClass, ApplicationContext applicationContext) {
        this.cls = cls;
        this.isTargetClass = isTargetClass;
        this.applicationContext = applicationContext;
    }

    public RemoteReferenceBean(Class<T> cls, ApplicationContext applicationContext) {
        this.cls = cls;
        this.isTargetClass = false;
        this.applicationContext = applicationContext;

    }

    @Override
    public void destroy() throws Exception {
        //
    }
}
