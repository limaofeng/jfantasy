package org.jfantasy.wx.framework.intercept;

import org.jfantasy.wx.framework.exception.WeiXinException;
import org.jfantasy.wx.framework.session.WeiXinSession;

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
