package com.fantasy.wx.intercept;

import com.fantasy.wx.exception.WeiXinException;
import com.fantasy.wx.session.WeiXinSession;

/**
 * 微信主动发送消息拦截器
 */
public interface WeiXinSendMessageInterceptor<T, E> {

    /**
     * 消息拦截器
     *
     * @param session    微信公众号
     * @param message    消息内容
     * @param to         接收人
     * @param invocation 回调处理
     * @throws WeiXinException
     */
    void intercept(WeiXinSession session, T message, E to, Invocation invocation) throws WeiXinException;

}
