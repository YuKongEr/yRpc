package com.yukong.yrpc.core.client.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author: yukong
 * @date: 2019/1/2 17:16
 */
public class JdkProxyInvoker extends AbstructRpcProxyInvoker implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] parameters) throws Throwable {
        return super.invoke(method, parameters);
    }
}
