package org.jfantasy.pay.order;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceRegistryRunner implements CommandLineRunner {

    @Value("${api.pay.local.rpc.title:未设置}")
    private String title;
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
        orderServiceRegistry.register(orderService.types(), this.title, this.description, this.host, this.port);
    }

}
