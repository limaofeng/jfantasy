package org.jfantasy.rpc.proxy;

import org.apache.commons.lang3.tuple.Pair;
import org.jfantasy.rpc.client.NettyClient;
import org.jfantasy.rpc.client.NettyClientFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.concurrent.TimeUnit;

public class RpcProxyFactory {

    private NettyClientFactory nettyClientFactory;

    public RpcProxyFactory(NettyClientFactory nettyClientFactory) {
        this.nettyClientFactory = nettyClientFactory;
    }

    @SuppressWarnings("unchecked")
    public <T> T proxyBean(Class<T> targetInterface,long timeoutInMillis){
        NettyClient client = nettyClientFactory.get(targetInterface);
        InvocationHandler rpcProxy = new RpcProxy(client,targetInterface,Pair.of(timeoutInMillis,TimeUnit.MILLISECONDS));
        return (T)Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{targetInterface}, rpcProxy);
    }

}
