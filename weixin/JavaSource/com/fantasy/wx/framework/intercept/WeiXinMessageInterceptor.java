package com.fantasy.wx.framework.intercept;

import com.fantasy.wx.framework.exception.WeiXinException;
import com.fantasy.wx.framework.message.WeiXinMessage;
import com.fantasy.wx.framework.session.WeiXinSession;

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
