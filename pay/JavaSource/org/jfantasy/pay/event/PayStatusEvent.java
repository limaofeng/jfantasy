package org.jfantasy.pay.event;

import org.jfantasy.pay.event.context.PayStatus;
import org.springframework.context.ApplicationEvent;

/**
 * 支付状态改变事件
 */
public class PayStatusEvent extends ApplicationEvent {

    public PayStatusEvent(PayStatus source) {
        super(source);
    }

    public PayStatus getSource() {
        return (PayStatus) super.getSource();
    }

}
