package com.fantasy.wx.event;

import com.fantasy.wx.message.content.Event;
import com.fantasy.wx.session.WeiXinSession;

/**
 * 点击菜单拉取消息时的事件推送监听接口
 */
public interface ClickEventListener extends WeiXinEventListener {

    public void onClick(WeiXinSession session,Event event);

}
