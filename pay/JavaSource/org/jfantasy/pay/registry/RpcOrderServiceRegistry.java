package org.jfantasy.pay.registry;

import org.jfantasy.pay.order.OrderServiceRegistry;
import org.jfantasy.pay.order.entity.enums.CallType;
import org.jfantasy.pay.service.OrderServerService;
import org.jfantasy.rpc.annotation.ServiceExporter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Properties;

@ServiceExporter(value = "rpcOrderServiceRegistry", targetInterface = OrderServiceRegistry.class)
public class RpcOrderServiceRegistry implements OrderServiceRegistry {

    private final OrderServerService orderServerService;

    @Autowired
    public RpcOrderServiceRegistry(OrderServerService orderServerService) {
        this.orderServerService = orderServerService;
    }

    @Override
    public void register(String type, String host, int port) {
        Properties props = new Properties();
        props.setProperty("host", host);
        props.put("port", String.valueOf(port));
        orderServerService.save(CallType.rpc, type, "RPC", props);
    }

    @Override
    public void register(String[] types, String host, int port) {
        for (String type : types) {
            this.register(type, host, port);
        }
    }

}
