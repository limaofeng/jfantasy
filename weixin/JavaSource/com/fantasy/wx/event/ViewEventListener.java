package com.fantasy.wx.event;

import com.fantasy.wx.message.content.Event;
import com.fantasy.wx.session.WeiXinSession;

/**
 * 点击菜单跳转链接时的事件推送监听接口
 */
public interface ViewEventListener extends WeiXinEventListener {

    public void onView(WeiXinSession session,Event event);

}
