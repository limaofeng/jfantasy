package org.jfantasy.pay.event.listener;

import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.product.order.Order;
import org.springframework.stereotype.Component;

@Component
public class TestOrderPayListener extends PayListener {

    @Override
    public boolean supportsOrderType(String orderType) {
        return "test".equalsIgnoreCase(orderType);
    }

    @Override
    void success(Payment payment, Order order) {
        System.out.println(order);
    }

    @Override
    void failure(Payment payment, Order order) {

    }
}