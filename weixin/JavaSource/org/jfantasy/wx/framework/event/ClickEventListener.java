package org.jfantasy.wx.framework.event;

import org.jfantasy.wx.framework.message.EventMessage;
import org.jfantasy.wx.framework.message.content.Event;
import org.jfantasy.wx.framework.session.WeiXinSession;

/**
 * 点击菜单拉取消息时的事件推送监听接口
 */
public interface ClickEventListener extends WeiXinEventListener {

    void onClick(WeiXinSession session,Event event,EventMessage message);

}
