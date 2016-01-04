package org.jfantasy.events;

import org.jfantasy.common.order.Order;
import org.springframework.context.ApplicationEvent;

public class ContentOrderEvent  extends ApplicationEvent {

    public ContentOrderEvent(Order source) {
        super(source);
    }

}
