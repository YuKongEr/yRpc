package cn.yukonga.yrpc.client.proxy;

import cn.yukonga.yrpc.client.RpcClientRequestPool;
import cn.yukonga.yrpc.client.RpcClientRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author: yukong
 * @date: 2019/1/2 17:17
 */
@Component
public class CglibProxyInvoker extends AbstructRpcProxyInvoker implements MethodInterceptor {
    @Autowired
    private RpcClientRequestPool rpcClientRequestPool;

    @Autowired
    private RpcClientRunner rpcClientRunner;
    @Override
    public Object intercept(Object o, Method method, Object[] parameters, MethodProxy methodProxy) throws Throwable {
        return super.invoke(method, parameters, rpcClientRunner, rpcClientRequestPool);
    }
}
