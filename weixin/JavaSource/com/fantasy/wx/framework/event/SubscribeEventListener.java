package com.fantasy.wx.framework.event;

import com.fantasy.wx.framework.message.content.Event;
import com.fantasy.wx.framework.session.WeiXinSession;

/**
 * 订阅事件消息监听接口
 */
public interface SubscribeEventListener  extends WeiXinEventListener{

    public void onSubscribe(WeiXinSession session,Event event);

}
