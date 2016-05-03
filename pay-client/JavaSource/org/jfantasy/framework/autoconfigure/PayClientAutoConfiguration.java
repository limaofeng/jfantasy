package org.jfantasy.framework.autoconfigure;

import org.jfantasy.pay.ons.Consumer;
import org.jfantasy.pay.order.OrderServiceRegistry;
import org.jfantasy.rpc.client.NettyClientFactory;
import org.jfantasy.rpc.proxy.RpcProxyFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PayClientAutoConfiguration {

    @Value("${pay.server.host:127.0.0.1}")
    private String host;
    @Value("${pay.server.port:9090}")
    private int port;

    @Bean(name = "orderServiceRegistry")
    public OrderServiceRegistry buildOrderServiceRegistry() {
        RpcProxyFactory rpcProxyFactory = new RpcProxyFactory(new NettyClientFactory(host, port));
        return rpcProxyFactory.proxyBean(OrderServiceRegistry.class, 10000);
    }

    @Bean(initMethod = "start")
    public Consumer consumer(){
        return new Consumer();
    }

}
