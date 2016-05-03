package org.jfantasy.rpc.client;

import org.jfantasy.rpc.dto.RpcRequest;
import org.jfantasy.rpc.dto.RpcResponse;
import org.apache.commons.lang3.tuple.Pair;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public interface IClient {
    void connect(InetSocketAddress socketAddress);

    RpcResponse syncSend(RpcRequest request) throws InterruptedException;

    RpcResponse asyncSend(RpcRequest request, Pair<Long, TimeUnit> timeout) throws InterruptedException;

    InetSocketAddress getRemoteAddress();

    void close();
}
