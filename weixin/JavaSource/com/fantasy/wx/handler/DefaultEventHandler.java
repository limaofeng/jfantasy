package com.fantasy.wx.handler;

import com.fantasy.wx.message.EmptyMessage;
import com.fantasy.wx.message.EventMessage;
import com.fantasy.wx.message.WeiXinMessage;
import com.fantasy.wx.session.WeiXinSession;

public class DefaultEventHandler extends EventWeiXinHandler{

    @Override
    protected WeiXinMessage handleEventMessage(WeiXinSession session, EventMessage message) {
        return EmptyMessage.get();
    }

}
