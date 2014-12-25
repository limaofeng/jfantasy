package com.fantasy.wx.message;

import java.util.Date;

/**
 * 微信消息工厂
 */
public class MessageFactory {

    /**
     * 文本消息
     *
     * @param msgId        消息id
     * @param toUserName   开发者微信号
     * @param fromUserName 发送方帐号（一个OpenID）
     * @param createTime   消息创建时间 （整型）
     * @param content      文本消息内容
     */
    public static TextMessage createTextMessage(String msgId, String toUserName, String fromUserName, Date createTime, String content) {
        return null;
    }

    public static ImageMessage createImageMessage(String msgId, String toUserName, String fromUserName, Date createTime, String content) {
        return null;
    }

    public static ImageMessage createVoiceMessage(String msgId, String toUserName, String fromUserName, Date createTime, String content) {
        return null;
    }

    public static ImageMessage createVideoMessage(String msgId, String toUserName, String fromUserName, Date createTime, String content) {
        return null;
    }



}
