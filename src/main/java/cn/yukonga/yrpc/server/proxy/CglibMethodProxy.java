package cn.yukonga.yrpc.server.proxy;

import cn.yukonga.yrpc.core.model.RpcRequest;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;

import java.lang.reflect.InvocationTargetException;

/**
 * @author: yukong
 * @date: 2019/1/2 09:36
 */
public class CglibMethodProxy implements MethodProxy{
    @Override
    public Object invoke(RpcRequest rpcRequest, Object targetBean) throws InvocationTargetException {
        String methodName = rpcRequest.getMethodName();
        Class<?>[] paramTypes = rpcRequest.getParamterTypes();
        Object[] params = rpcRequest.getParamters();
        FastClass serviceFastClass = FastClass.create(targetBean.getClass());
        FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, paramTypes);
        return serviceFastMethod.invoke(targetBean, params);
    }
}
