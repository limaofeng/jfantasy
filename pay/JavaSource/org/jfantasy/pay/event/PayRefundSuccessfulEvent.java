package org.jfantasy.pay.event;


import org.jfantasy.pay.event.context.PayContext;
import org.springframework.context.ApplicationEvent;

public class PayRefundSuccessfulEvent extends ApplicationEvent {

    public PayRefundSuccessfulEvent(PayContext source) {
        super(source);
    }
}
