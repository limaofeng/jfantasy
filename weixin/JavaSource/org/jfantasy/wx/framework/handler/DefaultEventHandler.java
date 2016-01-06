package org.jfantasy.wx.framework.handler;

import org.jfantasy.wx.framework.message.EmptyMessage;
import org.jfantasy.wx.framework.message.EventMessage;
import org.jfantasy.wx.framework.message.WeiXinMessage;
import org.jfantasy.wx.framework.session.WeiXinSession;

public class DefaultEventHandler extends EventWeiXinHandler{

    @Override
    protected WeiXinMessage handleEventMessage(WeiXinSession session, EventMessage message) {
        return EmptyMessage.get();
    }

}
