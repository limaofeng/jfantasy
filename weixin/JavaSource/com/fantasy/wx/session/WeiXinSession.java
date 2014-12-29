package com.fantasy.wx.session;


import com.fantasy.wx.message.content.*;

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
     * 发送图片消息
     *
     * @param content 图片消息
     * @param toUser  接收人
     */
    public void sendImageMessage(Image content, String toUser);

    /**
     * 发送语音消息
     *
     * @param content 语音消息
     * @param toUser  接收人
     */
    public void sendVoiceMessage(Voice content, String toUser);

    /**
     * 发送视频消息
     *
     * @param content 视频消息
     * @param toUser  接收人
     */
    public void sendVideoMessage(Video content, String toUser);

    /**
     * 发送音乐消息
     *
     * @param content 音乐消息
     * @param toUser  接收人
     */
    public void sendMusicMessage(Music content, String toUser);

    /**
     * 发送音乐消息
     *
     * @param content 图文消息
     * @param toUser  接收人
     */
    public void sendNewsMessage(News content, String toUser);

    /**
     * 发送文本消息
     *
     * @param content 文本消息
     * @param toUser  接收人
     */
    void sendTextMessage(String content, String toUser);

    /**
     * 获取当前公众号信息
     *
     * @return AccountDetails
     */
    AccountDetails getAccountDetails();

}
