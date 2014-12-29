package com.fantasy.wx.event;

import com.fantasy.wx.message.content.EventLocation;
import com.fantasy.wx.session.WeiXinSession;

/**
 * 微信事件消息监听接口
 */
public interface LocationEventListener extends WeiXinEventListener {

    public void onLocation(WeiXinSession session,EventLocation event);

}
