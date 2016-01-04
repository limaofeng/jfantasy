package org.jfantasy.wx.framework.event;

import org.jfantasy.wx.framework.message.EventMessage;
import org.jfantasy.wx.framework.message.content.Event;
import org.jfantasy.wx.framework.session.WeiXinSession;

/**
 * 点击菜单跳转链接时的事件推送监听接口
 */
public interface ViewEventListener extends WeiXinEventListener {

    public void onView(WeiXinSession session,Event event,EventMessage message);

}
