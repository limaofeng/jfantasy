package org.jfantasy.pay.event.listener;

import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.event.PayFailedEvent;
import org.jfantasy.pay.event.PaySuccessfulEvent;
import org.jfantasy.pay.event.context.PayContext;
import org.jfantasy.pay.product.order.Order;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;

/**
 * 支付监听
 */
public abstract class PayListener implements SmartApplicationListener {

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return eventType == PaySuccessfulEvent.class || eventType == PayFailedEvent.class;
    }

    @Override
    public boolean supportsSourceType(Class<?> sourceType) {
        return sourceType == PayContext.class;
    }

    public abstract boolean supportsOrderType(String orderType);

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        PayContext payContext = (PayContext) event.getSource();
        Payment payment = payContext.getPayment();
        Order order = payContext.getOrder();
        if (!supportsOrderType(payment.getOrderType())) {
            return;
        }
        if (Payment.Status.success == payContext.getPayment().getStatus()) {
            this.success(payment, order);
        } else if (Payment.Status.failure == payContext.getPayment().getStatus()) {
            this.failure(payment, order);
        }
    }

    abstract void success(Payment payment, Order order);

    abstract void failure(Payment payment, Order order);

}
