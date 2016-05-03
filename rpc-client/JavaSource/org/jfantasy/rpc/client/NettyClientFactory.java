package org.jfantasy.rpc.client;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

public class NettyClientFactory {

    private static ConcurrentHashMap<Class<?>, NettyClient> serviceClientMap = new ConcurrentHashMap<Class<?>, NettyClient>();

    private String host;
    private int port;

    public NettyClientFactory(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public NettyClient get(Class<?> targetInterface) {
        NettyClient client = serviceClientMap.get(targetInterface);
        if (client != null && !client.isClosed()) {
            return client;
        }
        //connect
        NettyClient newClient = new NettyClient();
        //TODO get from service registry
        newClient.connect(new InetSocketAddress(this.host, this.port));
        serviceClientMap.putIfAbsent(targetInterface, newClient);
        return newClient;
    }


}
