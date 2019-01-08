package cn.yukonga.yrpc.server.proxy;

import cn.yukonga.yrpc.core.model.RpcRequest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author: yukong
 * @date: 2019/1/2 09:36
 */
public class JdkMethodProxy implements MethodProxy{
    @Override
    public Object invoke(RpcRequest rpcRequest, Object targetBean) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        String methodName = rpcRequest.getMethodName();
        Class<?>[] paramTypes = rpcRequest.getParamterTypes();
        Object[] params = rpcRequest.getParamters();
        Method method = targetBean.getClass().getMethod(methodName, paramTypes);
        method.setAccessible(true);
        return method.invoke(targetBean, params);
    }
}
