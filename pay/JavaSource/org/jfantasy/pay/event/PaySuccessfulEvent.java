package org.jfantasy.pay.event;


import org.jfantasy.pay.event.context.PayContext;
import org.springframework.context.ApplicationEvent;

public class PaySuccessfulEvent extends ApplicationEvent {

    public PaySuccessfulEvent(PayContext source) {
        super(source);
    }
}
