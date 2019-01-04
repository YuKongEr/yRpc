package com.yukong.yrpc.client.proxy;

import com.yukong.yrpc.client.RpcClientRequestPool;
import com.yukong.yrpc.client.RpcClientRunner;
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
    public Object invoke(Method method, Object[] parameters, RpcClientRunner rpcClientRunner, RpcClientRequestPool rpcClientRequestPool) throws Exception{
        String requestId = UUID.randomUUID().toString();
        String className = method.getDeclaringClass().getName();
        String methodName = method.getName();
        Class<?>[] paramterTypes = method.getParameterTypes();
        Class<?> returnType = method.getReturnType();

        RpcRequest rpcRequest = new RpcRequest(requestId, className, methodName, paramterTypes, parameters);
        rpcClientRunner.send(rpcRequest);
        RpcResponse response = rpcClientRequestPool.getResponse(requestId);
        if (response == null){
            return null;
        }
        Object result = response.getResult();
        if (result == null){
            throw response.getException();
        }
        return result;
    }
}
