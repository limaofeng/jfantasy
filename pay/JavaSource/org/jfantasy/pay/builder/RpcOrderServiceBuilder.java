package org.jfantasy.pay.builder;


import org.jfantasy.pay.OrderServiceBuilder;
import org.jfantasy.pay.bean.OrderServer;
import org.jfantasy.pay.order.OrderService;
import org.jfantasy.pay.order.entity.enums.CallType;
import org.jfantasy.rpc.client.NettyClientFactory;
import org.jfantasy.rpc.proxy.RpcProxyFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RpcOrderServiceBuilder implements OrderServiceBuilder {

    private static final long timeoutInMillis = 10000;

    @Override
    public CallType getCallType() {
        return CallType.rpc;
    }

    @Override
    public OrderService build(Map props) {
        String host = props.get(OrderServer.PROPS_HOST).toString();
        String port = props.get(OrderServer.PROPS_PORT).toString();
        RpcProxyFactory rpcProxyFactory = new RpcProxyFactory(new NettyClientFactory(host, Integer.valueOf(port)));
        return rpcProxyFactory.proxyBean(org.jfantasy.pay.order.OrderService.class, timeoutInMillis);
    }

}
