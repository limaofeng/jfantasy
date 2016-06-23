package org.jfantasy.pay.builder;


import org.jfantasy.pay.OrderServiceBuilder;
import org.jfantasy.pay.bean.OrderServer;
import org.jfantasy.pay.order.OrderService;
import org.jfantasy.pay.order.entity.enums.CallType;
import org.jfantasy.rpc.client.NettyClientFactory;
import org.jfantasy.rpc.proxy.RpcProxyFactory;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class RpcOrderServiceBuilder implements OrderServiceBuilder {

    /**
     * 创建 OrderService 的超时时间
     */
    private static final long timeoutInMillis = 1000;

    @Override
    public CallType getCallType() {
        return CallType.rpc;
    }

    @Override
    public OrderService build(Properties props) {
        String host = props.getProperty(OrderServer.PROPS_HOST);
        int port = (int) props.get(OrderServer.PROPS_PORT);
        RpcProxyFactory rpcProxyFactory = new RpcProxyFactory(new NettyClientFactory(host, port));
        return rpcProxyFactory.proxyBean(org.jfantasy.pay.order.OrderService.class, timeoutInMillis);
    }

}
