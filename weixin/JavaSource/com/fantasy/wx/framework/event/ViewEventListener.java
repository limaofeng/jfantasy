package com.fantasy.wx.framework.event;

import com.fantasy.wx.framework.message.content.Event;
import com.fantasy.wx.framework.session.WeiXinSession;

/**
 * 点击菜单跳转链接时的事件推送监听接口
 */
public interface ViewEventListener extends WeiXinEventListener {

    public void onView(WeiXinSession session,Event event);

}
