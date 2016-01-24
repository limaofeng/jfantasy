package org.jfantasy.pay.event.context;

import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.product.order.Order;

public class PayContext {

    private Payment payment;

    private Order order;

    public PayContext(Payment payment, Order order) {
        this.payment = payment;
        this.order = order;
    }

    public Payment getPayment() {
        return payment;
    }

    public Order getOrder() {
        return order;
    }
}
