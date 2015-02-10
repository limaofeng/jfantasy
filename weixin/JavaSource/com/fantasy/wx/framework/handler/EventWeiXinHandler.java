package com.fantasy.wx.framework.handler;

import com.fantasy.wx.framework.message.*;
import com.fantasy.wx.framework.session.WeiXinSession;

/**
 * 事件消息处理器
 */
public abstract class EventWeiXinHandler extends AbstracWeiXinHandler {

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
    protected WeiXinMessage handleLinkMessage(WeiXinSession session, LinkMessage message) {
        return EmptyMessage.get();
    }

    @Override
    protected WeiXinMessage handleTextMessage(WeiXinSession session, TextMessage message) {
        return EmptyMessage.get();
    }
}
