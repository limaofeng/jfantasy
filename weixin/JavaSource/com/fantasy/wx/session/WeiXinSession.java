package com.fantasy.wx.session;


import com.fantasy.wx.message.WeiXinMessage;

import java.io.IOException;

/**
 * 微信 session 接口
 * 主要包含微信消息相关的公众号及订阅号内容
 */
public interface WeiXinSession {

    /**
     * 微信号的 appid
     *
     * @return String
     */
    String getId();

    /**
     * 发送消息接口
     *
     * @param message 消息对象
     * @throws IOException
     */
    void sendMessage(WeiXinMessage<?> message) throws IOException;

    /**
     * 获取当前公众号信息
     *
     * @return AccountDetails
     */
    AccountDetails getAccountDetails();

}
