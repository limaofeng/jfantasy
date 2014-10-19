package com.fantasy.wx.bean.req;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * 消息基类（普通用户 -> 公众帐号）
 * Created by zzzhong on 2014/6/17.
 */
@MappedSuperclass
public class BaseMessage {

    // 开发者微信号
    @Column(name = "TO_USER_NAME", length = 200)
    private String ToUserName;
    // 发送方帐号（一个OpenID）
    @Column(name = "FROM_USER_NAME")
    private String FromUserName;
    // 消息创建时间 （整型）
    @Column(name = "CREATE_TIME")
    private long CreateTime;
    // 消息类型（text/image/location/link）
    @Column(name = "MSG_TYPE")
    private String MsgType;
    // 消息id，64位整型
    @Column(name = "MSG_ID")
    private long MsgId;

    public String getToUserName() {
        return ToUserName;
    }

    public void setToUserName(String toUserName) {
        ToUserName = toUserName;
    }

    public String getFromUserName() {
        return FromUserName;
    }

    public void setFromUserName(String fromUserName) {
        FromUserName = fromUserName;
    }

    public long getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(long createTime) {
        CreateTime = createTime;
    }

    public String getMsgType() {
        return MsgType;
    }

    public void setMsgType(String msgType) {
        MsgType = msgType;
    }

    public long getMsgId() {
        return MsgId;
    }

    public void setMsgId(long msgId) {
        MsgId = msgId;
    }
}
