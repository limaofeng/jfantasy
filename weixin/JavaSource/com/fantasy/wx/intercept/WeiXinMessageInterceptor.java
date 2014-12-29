package com.fantasy.wx.intercept;

import com.fantasy.wx.exception.WeiXinException;
import com.fantasy.wx.message.WeiXinMessage;
import com.fantasy.wx.session.WeiXinSession;

/**
 * 微信消息拦截器
 */
public interface WeiXinMessageInterceptor {

    /**
     * 消息拦截器
     *
     * @param session    微信公众号
     * @param message    消息
     * @param invocation 调用
     * @return WeiXinMessage
     */
    WeiXinMessage intercept(WeiXinSession session, WeiXinMessage message, Invocation invocation) throws WeiXinException;

}
