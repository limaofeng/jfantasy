package org.jfantasy.pay.event;

import org.jfantasy.pay.event.context.PayContext;
import org.springframework.context.ApplicationEvent;

public class PayFailedEvent extends ApplicationEvent {

    public PayFailedEvent(PayContext source) {
        super(source);
    }
}
