package com.fantasy.wx.handler;

import com.fantasy.wx.message.TextMessage;
import com.fantasy.wx.message.WeiXinMessage;
import com.fantasy.wx.session.WeiXinSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 自动回复处理器
 */
public class AutoReplyTextHandler extends TextWeiXinHandler {

    private final static Log LOG = LogFactory.getLog(AutoReplyTextHandler.class);

    @Override
    protected WeiXinMessage handleTextMessage(WeiXinSession session, TextMessage message) {
        //TODO 自动回复功能，待完善
        LOG.debug("自动回复功能，待完善");
        return new TextMessage(message.getContent() + " == > 测试自动回复");
    }

}
