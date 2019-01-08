package cn.yukonga.yrpc.client;

import cn.yukonga.yrpc.core.model.RpcResponse;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author: yukong
 * @date: 2019/1/3 11:05
 */
@Component
public class RpcClientRequestPool {


    private final Logger logger = LoggerFactory.getLogger(getClass());


    private final Map<String, Promise<RpcResponse>> requestPool = new ConcurrentHashMap<>();

    /**
     * 添加异步请求
     * @param requestId 请求id
     * @param eventExecutor 任务执行器
     */
    public void addRequest(String requestId, EventExecutor eventExecutor) {
        requestPool.put(requestId, new DefaultPromise<RpcResponse>(eventExecutor));
    }

    /**
     * 获取请求结果， 等待10s
     * @param requestId 请求id
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws TimeoutException
     */
    public RpcResponse getResponse(String requestId) throws InterruptedException, ExecutionException, TimeoutException {
        Promise<RpcResponse> promise = requestPool.get(requestId);
        if (promise == null) {
            return null;
        }
        RpcResponse response = promise.get(60, TimeUnit.SECONDS);
        requestPool.remove(requestId);
        return response;
    }

    /**
     * 通知异步请求成功
     * @param requestId 请求id
     * @param rpcResponse 请求相应结果
     */
    public void notifyRequest(String requestId, RpcResponse rpcResponse){
        Promise<RpcResponse> promise = requestPool.get(requestId);
        if (promise != null){
            promise.setSuccess(rpcResponse);
        }
    }

}
