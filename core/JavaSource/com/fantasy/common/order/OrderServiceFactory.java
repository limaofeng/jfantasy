package com.fantasy.common.order;

import java.util.HashMap;
import java.util.Map;

public class OrderServiceFactory {

    private Map<String, OrderService> orderDetailsServices;

    public OrderServiceFactory() {
        orderDetailsServices = new HashMap<String, OrderService>();
        orderDetailsServices.put("TEST",new TestOrderDetailsService());
    }

    public OrderServiceFactory(Map<String, OrderService> orderDetailsServices) {
        this.orderDetailsServices = orderDetailsServices;
    }

    public void register(String type, OrderService orderDetailsService) {
        orderDetailsServices.put(type, orderDetailsService);
    }

    public OrderService get(String type) {
        return orderDetailsServices.get(type);
    }

}
