package com.fantasy.wx.event;

import com.fantasy.wx.message.content.Event;
import com.fantasy.wx.session.WeiXinSession;

/**
 * 用户已关注时的事件推送监听接口
 */
public interface ScanEventListener  extends WeiXinEventListener{

    public void onScan(WeiXinSession session,Event event);

}
