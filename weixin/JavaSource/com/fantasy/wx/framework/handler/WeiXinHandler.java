package com.fantasy.wx.framework.handler;

import com.fantasy.wx.framework.exception.WeiXinException;
import com.fantasy.wx.framework.message.WeiXinMessage;
import com.fantasy.wx.framework.session.WeiXinSession;

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
