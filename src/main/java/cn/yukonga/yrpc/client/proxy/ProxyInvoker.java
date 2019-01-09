package cn.yukonga.yrpc.client.proxy;

import cn.yukonga.yrpc.client.RpcClientRequestPool;
import cn.yukonga.yrpc.client.RpcClientRunner;

import java.lang.reflect.Method;

/**
 * @author : yukong
 */
public interface ProxyInvoker {

    Object invoke(Method method, Object[] parameters, RpcClientRunner rpcClientRunner, RpcClientRequestPool rpcClientRequestPool) throws Exception;

}
