package com.fantasy.wx.message;

import java.util.Date;

public abstract class AbstractWeiXinMessage<T> implements WeiXinMessage<T> {

    public AbstractWeiXinMessage(Long id, String fromUserName, Date createTime) {
        this.id = id;
        this.fromUserName = fromUserName;
        this.createTime = createTime;
    }

    public AbstractWeiXinMessage(T content) {
        this.content = content;
    }

    /**
     * 消息id
     */
    private Long id;
    /**
     * 发送方帐号（一个OpenID）
     */
    private String fromUserName;
    /**
     * 消息创建时间
     */
    private Date createTime;
    /**
     * 开发者微信号（原始ID）
     */
    private String toUserName;

    private T content;

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public String getFromUserName() {
        return this.fromUserName;
    }

    @Override
    public Date getCreateTime() {
        return this.createTime;
    }

    @Override
    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
