package com.fantasy.wx.handler;

import com.fantasy.wx.exception.WeiXinException;
import com.fantasy.wx.message.*;
import com.fantasy.wx.session.WeiXinSession;

/**
 * 微信处理器
 */
public abstract class AbstracWeiXinHandler implements WeiXinHandler {

    @Override
    public WeiXinMessage<?> handleMessage(WeiXinSession session, WeiXinMessage<?> message) throws WeiXinException {
        WeiXinMessage outMessage;
        if (message instanceof TextMessage) {
            outMessage = handleTextMessage(session, (TextMessage) message);
        } else if (message instanceof ImageMessage) {
            outMessage = handleImageMessage(session, (ImageMessage) message);
        } else if (message instanceof VoiceMessage) {
            outMessage = handleVoiceMessage(session, (VoiceMessage) message);
        } else if (message instanceof VideoMessage) {
            outMessage = handleVideoMessage(session, (VideoMessage) message);
        } else if (message instanceof LocationMessage) {
            outMessage = handleLocationMessage(session, (LocationMessage) message);
        } else if (message instanceof LinkMessage) {
            outMessage = handleLinkMessage(session, (LinkMessage) message);
        } else if (message instanceof EventMessage) {
            outMessage = handleEventMessage(session, (EventMessage) message);
        } else{
            throw new IllegalStateException("Unexpected WeiXin message type: " + message);
        }
        if (outMessage == null || outMessage == EmptyMessage.get()) {
            return null;
        }
        AbstractWeiXinMessage _outMessage = (AbstractWeiXinMessage) outMessage;
        _outMessage.setFromUserName(message.getToUserName());
        _outMessage.setToUserName(message.getFromUserName());
        return _outMessage;
    }

    protected abstract WeiXinMessage handleEventMessage(WeiXinSession session, EventMessage message);

    protected abstract WeiXinMessage handleImageMessage(WeiXinSession session, ImageMessage message);

    protected abstract WeiXinMessage handleVideoMessage(WeiXinSession session, VideoMessage message);

    protected abstract WeiXinMessage handleLocationMessage(WeiXinSession session, LocationMessage message);

    protected abstract WeiXinMessage handleVoiceMessage(WeiXinSession session, VoiceMessage message);

    protected abstract WeiXinMessage handleLinkMessage(WeiXinSession session, LinkMessage message);

    protected abstract WeiXinMessage handleTextMessage(WeiXinSession session, TextMessage message);


}
