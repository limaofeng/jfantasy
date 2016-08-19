package org.jfantasy.pay.order;


import org.jfantasy.rpc.config.NettyLocalSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceRegistryRunner implements CommandLineRunner {

    @Autowired
    public NettyLocalSettings nettyLocalSettings;

    @Autowired
    private OrderServiceRegistry orderServiceRegistry;

    @Autowired
    private OrderService orderService;

    @Override
    public void run(String... args) throws Exception {
        orderServiceRegistry.register(orderService.types(), nettyLocalSettings.getHost(), nettyLocalSettings.getPort());
    }

}
