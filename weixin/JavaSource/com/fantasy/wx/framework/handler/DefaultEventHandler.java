package com.fantasy.wx.framework.handler;

import com.fantasy.wx.framework.message.EmptyMessage;
import com.fantasy.wx.framework.message.EventMessage;
import com.fantasy.wx.framework.message.WeiXinMessage;
import com.fantasy.wx.framework.session.WeiXinSession;

public class DefaultEventHandler extends EventWeiXinHandler{

    @Override
    protected WeiXinMessage handleEventMessage(WeiXinSession session, EventMessage message) {
        return EmptyMessage.get();
    }

}
