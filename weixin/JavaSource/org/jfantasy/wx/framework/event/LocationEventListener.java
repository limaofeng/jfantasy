package org.jfantasy.wx.framework.event;

import org.jfantasy.wx.framework.message.EventMessage;
import org.jfantasy.wx.framework.message.content.EventLocation;
import org.jfantasy.wx.framework.session.WeiXinSession;

/**
 * 微信事件消息监听接口
 */
public interface LocationEventListener extends WeiXinEventListener {

    void onLocation(WeiXinSession session,EventLocation event,EventMessage message);

}
