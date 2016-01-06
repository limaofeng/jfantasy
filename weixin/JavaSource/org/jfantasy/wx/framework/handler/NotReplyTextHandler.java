package org.jfantasy.wx.framework.handler;

import org.jfantasy.wx.framework.message.EmptyMessage;
import org.jfantasy.wx.framework.message.TextMessage;
import org.jfantasy.wx.framework.message.WeiXinMessage;
import org.jfantasy.wx.framework.session.WeiXinSession;

public class NotReplyTextHandler extends TextWeiXinHandler {

    @Override
    protected WeiXinMessage handleTextMessage(WeiXinSession session, TextMessage message) {
        return EmptyMessage.get();
    }

}
