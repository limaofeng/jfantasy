package org.jfantasy.pay.event.listener;

import org.jfantasy.pay.order.entity.enums.RefundStatus;
import org.jfantasy.pay.bean.Order;
import org.jfantasy.pay.bean.Refund;
import org.jfantasy.pay.event.PayRefundNotifyEvent;
import org.jfantasy.pay.event.context.PayContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;


public abstract class PayRefundListener implements SmartApplicationListener {

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return eventType == PayRefundNotifyEvent.class;
    }

    @Override
    public boolean supportsSourceType(Class<?> sourceType) {
        return sourceType == PayContext.class;
    }

    public abstract boolean supportsOrderType(String orderType);

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        PayContext payContext = (PayContext) event.getSource();
        Refund refund = payContext.getRefund();
        Order order = payContext.getOrder();
        if (!supportsOrderType(order.getType())) {
            return;
        }
        if (RefundStatus.success == payContext.getRefund().getStatus()) {
            this.success(refund, order);
        } else if (RefundStatus.failure == payContext.getRefund().getStatus()) {
            this.failure(refund, order);
        }
    }

    public abstract void success(Refund refund, Order order);

    public abstract void failure(Refund refund, Order order);

}
