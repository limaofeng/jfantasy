package org.jfantasy.pay.event;


import org.jfantasy.pay.event.context.PayContext;
import org.springframework.context.ApplicationEvent;

public class PayRefundNotifyEvent extends ApplicationEvent {

    public PayRefundNotifyEvent(PayContext source) {
        super(source);
    }

}
