package com.fantasy.wx.handler;

import com.fantasy.wx.message.*;
import com.fantasy.wx.session.WeiXinSession;

/**
 * 文本消息 hander
 */
public abstract class TextWeiXinHandler extends AbstracWeiXinHandler {

    @Override
    protected WeiXinMessage handleImageMessage(WeiXinSession session, ImageMessage message) {
        return EmptyMessage.get();
    }

    @Override
    protected WeiXinMessage handleVideoMessage(WeiXinSession session, VideoMessage message) {
        return EmptyMessage.get();
    }

    @Override
    protected WeiXinMessage handleLocationMessage(WeiXinSession session, LocationMessage message) {
        return EmptyMessage.get();
    }

    @Override
    protected WeiXinMessage handleVoiceMessage(WeiXinSession session, VoiceMessage message) {
        return EmptyMessage.get();
    }

    @Override
    protected WeiXinMessage handleEventMessage(WeiXinSession session, EventMessage message) {
        return EmptyMessage.get();
    }

    @Override
    protected WeiXinMessage handleLinkMessage(WeiXinSession session, LinkMessage message) {
        return EmptyMessage.get();
    }

}
