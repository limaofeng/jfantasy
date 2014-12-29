package com.fantasy.wx.handler;

import com.fantasy.wx.exception.WeiXinException;
import com.fantasy.wx.message.WeiXinMessage;
import com.fantasy.wx.session.WeiXinSession;

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
