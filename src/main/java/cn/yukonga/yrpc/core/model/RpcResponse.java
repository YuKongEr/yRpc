package cn.yukonga.yrpc.core.model;

/**
 * @author : yukong
  * rpc相应
 */
public class RpcResponse {

    /**
     * 对应的请求id
     */
    private String requestId;

    /**
     * 异常
     */
    private Exception exception;

    /**
     * 结果
     */
    private Object result;


    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "RpcResponse{" +
                "requestId='" + requestId + '\'' +
                ", exception=" + exception +
                ", result=" + result +
                '}';
    }
}
