package cn.yukonga.yrpc.core.model;

import java.util.Arrays;

/**
 * @author : yukong
  * rpc请求信息
 */
public class RpcRequest {

    /**
     * 请求id
     */
    private String requestId;

    /**
     * 调用的类名
     */
    private String className;

    /**
     * 调用的方法名
     */
    private String methodName;

    /**
     * 调用参数类型
     */
    private Class<?>[] paramterTypes;

    /**
     * 调用参数值
     */
    private Object[] paramters;


    public RpcRequest() {
    }

    public RpcRequest(String requestId, String className, String methodName, Class<?>[] paramterTypes, Object[] paramters) {
        this.requestId = requestId;
        this.className = className;
        this.methodName = methodName;
        this.paramterTypes = paramterTypes;
        this.paramters = paramters;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParamterTypes() {
        return paramterTypes;
    }

    public void setParamterTypes(Class<?>[] paramterTypes) {
        this.paramterTypes = paramterTypes;
    }

    public Object[] getParamters() {
        return paramters;
    }

    public void setParamters(Object[] paramters) {
        this.paramters = paramters;
    }

    @Override
    public String toString() {
        return "RpcRequest{" +
                "requestId='" + requestId + '\'' +
                ", className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", paramterTypes=" + Arrays.toString(paramterTypes) +
                ", paramters=" + Arrays.toString(paramters) +
                '}';
    }
}
