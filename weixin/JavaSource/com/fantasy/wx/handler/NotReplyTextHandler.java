package com.fantasy.wx.handler;

import com.fantasy.wx.message.EmptyMessage;
import com.fantasy.wx.message.TextMessage;
import com.fantasy.wx.message.WeiXinMessage;
import com.fantasy.wx.session.WeiXinSession;

public class NotReplyTextHandler extends TextWeiXinHandler {

    @Override
    protected WeiXinMessage handleTextMessage(WeiXinSession session, TextMessage message) {
        return EmptyMessage.get();
    }

}
