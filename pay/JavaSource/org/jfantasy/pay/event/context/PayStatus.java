package org.jfantasy.pay.event.context;


import org.jfantasy.pay.bean.Order;
import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.order.entity.enums.PaymentStatus;

public class PayStatus {

    private PaymentStatus status;
    private Payment payment;
    private Order order;

    public PayStatus(PaymentStatus status, Payment payment, Order order) {
        this.status = status;
        this.payment = payment;
        this.order = order;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

}
