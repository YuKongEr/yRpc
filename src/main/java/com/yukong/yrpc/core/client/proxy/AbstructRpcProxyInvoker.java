package com.yukong.yrpc.core.client.proxy;

import com.yukong.yrpc.core.model.RpcRequest;
import com.yukong.yrpc.core.model.RpcResponse;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * @author: yukong
 * @date: 2019/1/2 16:38
 */
public class AbstructRpcProxyInvoker implements ProxyInvoker{


    @Override
    public Object invoke(Method method, Object[] parameters) {
        String requestId = UUID.randomUUID().toString();
        String className = method.getDeclaringClass().getName();
        String methodName = method.getName();
        Class<?>[] paramterTypes = method.getParameterTypes();
        Class<?> returnType = method.getReturnType();

        RpcRequest rpcRequest = new RpcRequest(requestId, className, methodName, paramterTypes, parameters);

        // todo 发起netty请求
        /* RpcResponse response = requestPool.getResponse(requestId);
        if (response == null){
            return null;
        }*/
        return null;
    }
}
