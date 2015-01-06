package com.fantasy.wx.framework.intercept;


import com.fantasy.wx.framework.exception.WeiXinException;
import com.fantasy.wx.framework.message.WeiXinMessage;

public interface Invocation {

    WeiXinMessage invoke() throws WeiXinException;

}
