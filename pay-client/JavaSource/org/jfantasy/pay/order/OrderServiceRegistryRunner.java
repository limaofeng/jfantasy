package org.jfantasy.pay.order;


import org.jfantasy.framework.autoconfigure.NettyLocalSettings;
import org.jfantasy.pay.order.entity.enums.CallType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class OrderServiceRegistryRunner implements CommandLineRunner {

    @Bean
    @ConfigurationProperties(prefix = "netty.local")
    public NettyLocalSettings nettyLocalSettings() {
        return new NettyLocalSettings();
    }

    @Autowired
    private OrderServiceRegistry orderServiceRegistry;

    @Autowired
    private OrderService orderService;

    @Override
    public void run(String... args) throws Exception {
        Properties props = new Properties();
        props.setProperty("host", nettyLocalSettings().getHost());
        props.put("port", nettyLocalSettings().getPort());
        orderServiceRegistry.register(CallType.restful, orderService.types(), "", props);
    }

}
