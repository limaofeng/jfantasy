package org.jfantasy.wx.event;

import org.jfantasy.wx.framework.message.EventMessage;
import org.springframework.context.ApplicationEvent;

/**
 * 微信事件消息事件(spring事件机制)
 */
public class WeiXinEventMessageEvent extends ApplicationEvent {

    public WeiXinEventMessageEvent(EventMessage message) {
        super(message);
    }
}
