package com.fantasy.wx.framework.event;

import com.fantasy.wx.framework.message.content.Event;
import com.fantasy.wx.framework.session.WeiXinSession;

/**
 * 用户已关注时的事件推送监听接口
 */
public interface ScanEventListener  extends WeiXinEventListener{

    public void onScan(WeiXinSession session,Event event);

}
