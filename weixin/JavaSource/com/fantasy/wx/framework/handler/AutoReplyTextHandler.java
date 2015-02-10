package com.fantasy.wx.framework.handler;

import com.fantasy.wx.framework.message.EmptyMessage;
import com.fantasy.wx.framework.message.TextMessage;
import com.fantasy.wx.framework.message.WeiXinMessage;
import com.fantasy.wx.framework.session.WeiXinSession;
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
        return EmptyMessage.get();
    }

}
