package org.jfantasy.wx.listener;


import org.jfantasy.wx.event.WeiXinEventMessageEvent;
import org.jfantasy.wx.framework.message.EventMessage;
import org.springframework.context.ApplicationListener;

public abstract class WeiXinEventMessageApplicationListener implements ApplicationListener<WeiXinEventMessageEvent> {

    abstract boolean supportsEventType(EventMessage.EventType eventType);

    @Override
    public void onApplicationEvent(WeiXinEventMessageEvent event) {
        EventMessage message = (EventMessage) event.getSource();
        if (supportsEventType(message.getEventType())) {
            this.onEventMessage(message);
        }
    }

    abstract void onEventMessage(EventMessage message);

}
