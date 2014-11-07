package com.fantasy.wx.service;

import com.fantasy.wx.bean.req.Message;

/**
 * Created by zzzhong on 2014/10/17.
 */
public interface IEventService {

    public String focusOnEven(Message em);
    public String textMessage(Message tm);
    public String event(Message em);
}
