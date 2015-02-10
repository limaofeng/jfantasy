package com.fantasy.wx.framework.message;

import java.util.Date;

/**
 * 空消息
 */
public class EmptyMessage implements WeiXinMessage {

    private final static EmptyMessage message = new EmptyMessage();

    public static EmptyMessage get() {
        return message;
    }

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public String getFromUserName() {
        return null;
    }

    @Override
    public Date getCreateTime() {
        return null;
    }

    @Override
    public Object getContent() {
        return null;
    }

    @Override
    public String getToUserName() {
        return null;
    }

}
