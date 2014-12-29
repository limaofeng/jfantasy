package com.fantasy.wx.event;

import com.fantasy.wx.message.content.Event;
import com.fantasy.wx.session.WeiXinSession;

/**
 * 订阅事件消息监听接口
 */
public interface SubscribeEventListener  extends WeiXinEventListener{

    public void onSubscribe(WeiXinSession session,Event event);

}
