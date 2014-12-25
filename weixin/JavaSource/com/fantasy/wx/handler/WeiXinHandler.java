package com.fantasy.wx.handler;

import com.fantasy.wx.WeiXinMessage;
import com.fantasy.wx.session.WeiXinSession;

public interface WeiXinHandler {

    /**
     * 消息处理类
     *
     * @param session 微信回话
     * @param message 微信消息
     * @throws Exception
     */
    void handleMessage(WeiXinSession session, WeiXinMessage<?> message) throws Exception;

}
