package org.jfantasy.pay.event;

import org.jfantasy.pay.event.context.PayContext;
import org.springframework.context.ApplicationEvent;

/**
 * 支付通知事件
 */
public class PayNotifyEvent extends ApplicationEvent {

    public PayNotifyEvent(PayContext source) {
        super(source);
    }
}