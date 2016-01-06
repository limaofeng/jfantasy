package org.jfantasy.wx.framework.event;

import org.jfantasy.wx.framework.message.EventMessage;
import org.jfantasy.wx.framework.message.content.Event;
import org.jfantasy.wx.framework.session.WeiXinSession;

/**
 * 订阅事件消息监听接口
 */
public interface SubscribeEventListener  extends WeiXinEventListener{

    void onSubscribe(WeiXinSession session,Event event,EventMessage message);

}
