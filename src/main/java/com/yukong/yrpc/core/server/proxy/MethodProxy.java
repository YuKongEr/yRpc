package com.yukong.yrpc.core.server.proxy;

import com.yukong.yrpc.core.model.RpcRequest;

import java.lang.reflect.InvocationTargetException;

/**
 * @author: yukong
 * @date: 2019/1/2 09:35
 */
public interface MethodProxy {

    /**
     * 方法执行代理
     * @param target
     * @return
     */
    Object invoke(RpcRequest rpcRequest, Object target) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException;

    static MethodProxy getMethodProxy() {
        return getMethodProxy(false);
    }

    static MethodProxy getMethodProxy(Boolean isProxyTargetClass) {
        if(isProxyTargetClass) {
            return new CglibMethodProxy();
        }
        return new JdkMethodProxy();
    }

}
