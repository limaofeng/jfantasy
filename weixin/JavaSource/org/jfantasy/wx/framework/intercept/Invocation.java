package org.jfantasy.wx.framework.intercept;


import org.jfantasy.wx.framework.exception.WeiXinException;
import org.jfantasy.wx.framework.message.WeiXinMessage;

public interface Invocation {

    WeiXinMessage invoke() throws WeiXinException;

}
