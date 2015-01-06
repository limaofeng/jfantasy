package com.fantasy.wx.framework.handler;

import com.fantasy.wx.framework.message.EmptyMessage;
import com.fantasy.wx.framework.message.TextMessage;
import com.fantasy.wx.framework.message.WeiXinMessage;
import com.fantasy.wx.framework.session.WeiXinSession;

public class NotReplyTextHandler extends TextWeiXinHandler {

    @Override
    protected WeiXinMessage handleTextMessage(WeiXinSession session, TextMessage message) {
        return EmptyMessage.get();
    }

}
