package cn.yukonga.yrpc.server.proxy;

import cn.yukonga.yrpc.core.model.RpcRequest;

import java.lang.reflect.InvocationTargetException;

/**
 * @author : yukong
  */
public interface MethodProxy {

    /**
     * 方法执行代理
     * @param target 代理目标对象
     * @param rpcRequest 远程请求参数包装类
     * @throws InvocationTargetException 抛出InvocationTargetException
     * @throws IllegalAccessException 抛出IllegalAccessException
     * @throws NoSuchMethodException 抛出NoSuchMethodException
     * @return 执行结果
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
