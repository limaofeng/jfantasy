package org.jfantasy.pay.order;

import org.jfantasy.pay.order.entity.OrderDetails;
import org.jfantasy.pay.order.entity.OrderKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DemoTestOrderService implements CommandLineRunner {

    @Autowired
    private OrderService orderService;

    @Override
    public void run(String... strings) throws Exception {
        OrderDetails order = orderService.loadOrder(OrderKey.newInstance("test:T0000001"));
        System.out.println(order.toString());
    }
}
