package org.jfantasy.pay.event.listener;

import org.jfantasy.pay.bean.Order;
import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.event.PayNotifyEvent;
import org.jfantasy.pay.event.context.PayContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.scheduling.annotation.Async;

/**
 * 采用异步方式触发 - 支付监听
 */
public abstract class PayListener implements SmartApplicationListener {

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return eventType == PayNotifyEvent.class;
    }

    @Override
    public boolean supportsSourceType(Class<?> sourceType) {
        return sourceType == PayContext.class;
    }

    public abstract boolean supportsOrderType(String orderType);

    @Override
    @Async
    public void onApplicationEvent(ApplicationEvent event) {
        PayContext payContext = (PayContext) event.getSource();
        Payment payment = payContext.getPayment();
        Order order = payContext.getOrder();
        if (!supportsOrderType(order.getType())) {
            return;
        }
        this.on(payment, order);
    }

    public abstract void on(Payment payment, Order order);

}