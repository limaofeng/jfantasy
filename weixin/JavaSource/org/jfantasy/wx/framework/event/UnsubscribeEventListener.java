package org.jfantasy.wx.framework.event;

import org.jfantasy.wx.framework.message.EventMessage;
import org.jfantasy.wx.framework.message.content.Event;
import org.jfantasy.wx.framework.session.WeiXinSession;

/**
 * 取消订阅事件消息监听接口
 */
public interface UnsubscribeEventListener  extends WeiXinEventListener{

    void onUnsubscribe(WeiXinSession session,Event event,EventMessage message);

}
