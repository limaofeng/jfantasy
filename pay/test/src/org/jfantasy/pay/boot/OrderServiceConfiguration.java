package org.jfantasy.pay.boot;

import org.jfantasy.pay.order.OrderServiceFactory;
import org.jfantasy.pay.order.TestOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceConfiguration implements CommandLineRunner {

    @Autowired
    private OrderServiceFactory orderServiceFactory;

    @Override
    public void run(String... args) throws Exception {
        orderServiceFactory.register("test",new TestOrderService());
    }

}
