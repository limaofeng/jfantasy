package org.jfantasy.wx.event;


import org.jfantasy.wx.framework.message.WeiXinMessage;
import org.springframework.context.ApplicationEvent;

/**
 * 微信消息事件(spring事件机制)
 */
public class WeiXinMessageEvent extends ApplicationEvent {

    public WeiXinMessageEvent(WeiXinMessage message) {
        super(message);
    }
}
