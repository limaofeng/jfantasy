package org.jfantasy.pay.order;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceRegistryRunner implements CommandLineRunner {

    @Value("${pay.server.title:未设置}")
    private String title;
    @Value("${pay.server.description:未设置}")
    private String description;
    @Value("${rpc.server.host:127.0.0.1}")
    private String host;
    @Value("${rpc.server.port:9090}")
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
