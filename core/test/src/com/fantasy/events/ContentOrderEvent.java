package com.fantasy.events;

import com.fantasy.common.order.Order;
import org.springframework.context.ApplicationEvent;

public class ContentOrderEvent  extends ApplicationEvent {

    public ContentOrderEvent(Order source) {
        super(source);
    }

}
