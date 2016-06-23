package org.jfantasy.pay.order;


import org.jfantasy.pay.order.entity.enums.CallType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class OrderServiceRegistryRunner implements CommandLineRunner {

    @Value("${api.pay.local.rpc.description:未设置}")
    private String description;
    @Value("${api.pay.local.rpc.host:127.0.0.1}")
    private String host;
    @Value("${api.pay.local.rpc.port:9090}")
    private int port;

    @Autowired
    private OrderServiceRegistry orderServiceRegistry;

    @Autowired
    private OrderService orderService;

    @Override
    public void run(String... args) throws Exception {
        Properties props = new Properties();
        props.setProperty("host", host);
        props.put("port", port);
        orderServiceRegistry.register(CallType.rpc, orderService.types(), this.description, props);
    }

}
