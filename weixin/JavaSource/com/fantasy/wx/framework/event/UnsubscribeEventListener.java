package com.fantasy.wx.framework.event;

import com.fantasy.wx.framework.message.content.Event;
import com.fantasy.wx.framework.session.WeiXinSession;

/**
 * 取消订阅事件消息监听接口
 */
public interface UnsubscribeEventListener  extends WeiXinEventListener{

    public void onUnsubscribe(WeiXinSession session,Event event);

}
