package org.jfantasy.rpc.proxy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfantasy.rpc.dto.RpcRequest;
import org.jfantasy.rpc.dto.RpcResponse;
import org.apache.commons.lang3.tuple.Pair;
import org.jfantasy.rpc.client.NettyClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 利用代理优化远程调用
 * 使之像本地调用一样
 */
public class RpcProxy implements InvocationHandler {

    private final static Log LOG = LogFactory.getLog(RpcProxy.class);

    private NettyClient nettyClient;

    private Pair<Long, TimeUnit> timeout;

    private Class<?> targetInterface;

    public RpcProxy() {
    }

    RpcProxy(NettyClient nettyClient, Class<?> targetInterface, Pair<Long, TimeUnit> timeout) {
        this.nettyClient = nettyClient;
        this.timeout = timeout;
        this.targetInterface = targetInterface;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest request = new RpcRequest(); // 创建并初始化 RPC 请求
        request.setTraceId(UUID.randomUUID().toString());
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParameters(args);

        if (method.getDeclaringClass() != this.targetInterface) {
            LOG.error("Invalid invoke to " + method.toString());
            return null;
        }

        RpcResponse response;
        if (timeout == null) {
            response = nettyClient.syncSend(request);
        } else {
            response = nettyClient.asyncSend(request, timeout);
        }
        if (response.isError()) {
            throw response.getError();
        } else {
            return response.getResult();
        }
    }
}
