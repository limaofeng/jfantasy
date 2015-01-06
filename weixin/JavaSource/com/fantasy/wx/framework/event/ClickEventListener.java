package com.fantasy.wx.framework.event;

import com.fantasy.wx.framework.message.EventMessage;
import com.fantasy.wx.framework.message.content.Event;
import com.fantasy.wx.framework.session.WeiXinSession;

/**
 * 点击菜单拉取消息时的事件推送监听接口
 */
public interface ClickEventListener extends WeiXinEventListener {

    public void onClick(WeiXinSession session,Event event,EventMessage message);

}
