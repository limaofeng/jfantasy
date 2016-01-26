package org.jfantasy.pay.event;

import org.jfantasy.pay.event.context.PayContext;
import org.springframework.context.ApplicationEvent;

public class PayRefundFailedEvent extends ApplicationEvent {

    public PayRefundFailedEvent(PayContext source) {
        super(source);
    }
}
