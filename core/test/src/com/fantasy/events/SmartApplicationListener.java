package com.fantasy.events;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;

public interface SmartApplicationListener extends ApplicationListener<ApplicationEvent>, Ordered {

    //如果实现支持该事件类型 那么返回true
    boolean supportsEventType(Class<? extends ApplicationEvent> eventType);

    //如果实现支持“目标”类型，那么返回true
    boolean supportsSourceType(Class<?> sourceType);

    //顺序，即监听器执行的顺序，值越小优先级越高
    int getOrder();

}
