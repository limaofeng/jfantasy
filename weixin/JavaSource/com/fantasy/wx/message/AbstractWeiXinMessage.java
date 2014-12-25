package com.fantasy.wx.message;

import com.fantasy.wx.WeiXinMessage;

import java.util.Date;

public abstract class AbstractWeiXinMessage<T> implements WeiXinMessage<T> {

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
}
