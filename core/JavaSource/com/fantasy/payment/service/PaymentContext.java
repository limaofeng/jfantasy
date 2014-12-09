package com.fantasy.payment.service;

import com.fantasy.payment.bean.Payment;
import com.fantasy.payment.bean.PaymentConfig;
import com.fantasy.payment.product.PaymentProduct;

/**
 * 支付上下文对象
 */
public class PaymentContext {

    private static ThreadLocal<PaymentContext> threadLocal = new ThreadLocal<PaymentContext>();

    private OrderDetails orderDetails;

    private Payment payment;

    private PaymentConfig paymentConfig;

    private PaymentProduct paymentProduct;

    public static PaymentContext newInstall() {
        PaymentContext context = getContext();
        if (context == null) {
            setContext(new PaymentContext());
        }
        return getContext();
    }

    private static void setContext(PaymentContext context) {
        threadLocal.set(context);
    }

    public static PaymentContext getContext() {
        return threadLocal.get();
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
        this.paymentConfig = payment.getPaymentConfig();
    }

    public OrderDetails getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(OrderDetails orderDetails) {
        this.orderDetails = orderDetails;
    }

    public PaymentConfig getPaymentConfig() {
        return paymentConfig;
    }

    public PaymentProduct getPaymentProduct() {
        return paymentProduct;
    }

    public void setPaymentProduct(PaymentProduct paymentProduct) {
        this.paymentProduct = paymentProduct;
    }
}
