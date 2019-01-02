package com.yukong.yrpc.core.client.proxy;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author: yukong
 * @date: 2019/1/2 17:17
 */
public class CglibProxyInvoker extends AbstructRpcProxyInvoker implements MethodInterceptor {
    @Override
    public Object intercept(Object o, Method method, Object[] parameters, MethodProxy methodProxy) throws Throwable {
        return super.invoke(method, parameters);
    }
}
