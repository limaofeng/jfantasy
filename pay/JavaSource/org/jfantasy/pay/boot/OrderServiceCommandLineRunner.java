package org.jfantasy.pay.boot;

import org.jfantasy.pay.service.OrderServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceCommandLineRunner implements CommandLineRunner {

    @Autowired
    private OrderServerService orderServerService;

    @Override
    public void run(String... args) throws Exception {
        orderServerService.register();
    }

}
