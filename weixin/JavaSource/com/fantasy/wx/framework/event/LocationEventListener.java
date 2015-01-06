package com.fantasy.wx.framework.event;

import com.fantasy.wx.framework.message.content.EventLocation;
import com.fantasy.wx.framework.session.WeiXinSession;

/**
 * 微信事件消息监听接口
 */
public interface LocationEventListener extends WeiXinEventListener {

    public void onLocation(WeiXinSession session,EventLocation event);

}
