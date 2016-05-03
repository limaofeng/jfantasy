package org.jfantasy.pay.event.context;

import org.jfantasy.pay.bean.Order;
import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.bean.Refund;

public class PayContext {

    private Payment payment;
    private Refund refund;
    private Order order;

    public PayContext(Refund refund, Order order) {
        this.refund = refund;
        this.payment = refund.getPayment();
        this.order = order;
    }

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

    public Refund getRefund() {
        return refund;
    }

}
