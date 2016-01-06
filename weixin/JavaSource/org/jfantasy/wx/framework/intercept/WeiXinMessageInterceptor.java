package org.jfantasy.wx.framework.intercept;

import org.jfantasy.wx.framework.exception.WeiXinException;
import org.jfantasy.wx.framework.message.WeiXinMessage;
import org.jfantasy.wx.framework.session.WeiXinSession;

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
