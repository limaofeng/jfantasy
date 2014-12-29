package com.fantasy.wx.message;

import java.util.Date;

/**
 * 微信消息接口
 */
public interface WeiXinMessage<T> {

    /**
     * MsgId	消息id，64位整型
     *
     * @return id
     */
    Long getId();

    /**
     * 发送方帐号（一个OpenID）
     *
     * @return String
     */
    String getFromUserName();

    /**
     * 消息创建时间 （整型）
     *
     * @return date
     */
    Date getCreateTime();

    /**
     * 获取微信内容
     *
     * @return T
     */
    public T getContent();

    /**
     * 开发者微信号 (微信原始ID)
     *
     * @return String
     */
    public String getToUserName();

}
