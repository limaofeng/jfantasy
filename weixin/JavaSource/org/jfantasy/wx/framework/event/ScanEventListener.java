package org.jfantasy.wx.framework.event;

import org.jfantasy.wx.framework.message.EventMessage;
import org.jfantasy.wx.framework.message.content.Event;
import org.jfantasy.wx.framework.session.WeiXinSession;

/**
 * 用户已关注时的事件推送监听接口
 */
public interface ScanEventListener  extends WeiXinEventListener{

    public void onScan(WeiXinSession session,Event event,EventMessage message);

}
