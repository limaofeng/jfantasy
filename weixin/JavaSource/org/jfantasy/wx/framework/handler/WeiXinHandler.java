package org.jfantasy.wx.framework.handler;

import org.jfantasy.wx.framework.exception.WeiXinException;
import org.jfantasy.wx.framework.message.WeiXinMessage;
import org.jfantasy.wx.framework.session.WeiXinSession;

public interface WeiXinHandler {

    /**
     * 消息处理类
     *
     * @param session 微信回话
     * @param message 微信消息
     * @throws WeiXinException
     */
    WeiXinMessage<?> handleMessage(WeiXinSession session, WeiXinMessage<?> message) throws WeiXinException;

}
