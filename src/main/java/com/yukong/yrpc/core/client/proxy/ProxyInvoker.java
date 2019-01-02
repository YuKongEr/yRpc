package com.yukong.yrpc.core.client.proxy;

import java.lang.reflect.Method;

/**
 * @author: yukong
 * @date: 2019/1/2 16:36
 */
public interface ProxyInvoker {

    Object invoke(Method method, Object[] parameters);

}
