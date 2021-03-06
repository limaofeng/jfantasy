package org.jfantasy.wx.framework.handler;

import org.jfantasy.wx.framework.message.EmptyMessage;
import org.jfantasy.wx.framework.message.TextMessage;
import org.jfantasy.wx.framework.message.WeiXinMessage;
import org.jfantasy.wx.framework.session.WeiXinSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 自动回复处理器
 */
public class AutoReplyTextHandler extends TextWeiXinHandler {

    private final static Log LOG = LogFactory.getLog(AutoReplyTextHandler.class);

    private List<AutoReplyHandler> handlers = new ArrayList<AutoReplyHandler>();

    @Override
    protected WeiXinMessage handleTextMessage(WeiXinSession session, TextMessage message) {
        String keyword = message.getContent();
        for (AutoReplyHandler handler : handlers) {
            if (handler.handle(keyword)) {
                LOG.debug(keyword + " => " + handler);
                return handler.autoReply(keyword);
            }
        }
        return new EmptyMessage();
    }

    public void setHandlers(List<AutoReplyHandler> handlers) {
        this.handlers = handlers;
    }

}
