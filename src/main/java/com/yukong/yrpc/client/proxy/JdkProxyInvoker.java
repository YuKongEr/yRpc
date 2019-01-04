package com.yukong.yrpc.client.proxy;

import com.yukong.yrpc.client.RpcClientRequestPool;
import com.yukong.yrpc.client.RpcClientRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author: yukong
 * @date: 2019/1/2 17:16
 */
@Component
public class JdkProxyInvoker extends AbstructRpcProxyInvoker implements InvocationHandler {
    @Autowired
    private RpcClientRequestPool rpcClientRequestPool;

    @Autowired
    private RpcClientRunner rpcClientRunner;
    @Override
    public Object invoke(Object proxy, Method method, Object[] parameters) throws Throwable {
        return super.invoke(method, parameters, rpcClientRunner, rpcClientRequestPool);
    }
}
