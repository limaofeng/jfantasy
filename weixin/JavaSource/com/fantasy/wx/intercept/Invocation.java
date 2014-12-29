package com.fantasy.wx.intercept;


import com.fantasy.wx.exception.WeiXinException;
import com.fantasy.wx.message.WeiXinMessage;

public interface Invocation {

    WeiXinMessage invoke() throws WeiXinException;

}
