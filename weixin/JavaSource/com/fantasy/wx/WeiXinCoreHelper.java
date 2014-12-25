package com.fantasy.wx;

import com.fantasy.wx.exception.WeiXinException;
import com.fantasy.wx.session.AccountDetails;
import com.fantasy.wx.session.WeiXinSession;

import javax.servlet.http.HttpServletRequest;

/**
 * 微信签名相关接口
 */
public interface WeiXinCoreHelper {

    /**
     * 注册公众号服务，如果账号信息有更改需要重新调用该方法
     *
     * @param accountDetails 账号信息
     */
    public void register(AccountDetails accountDetails);

    /**
     * 解析接收到的消息
     *
     * @param session session
     * @param request HTTP请求
     * @return WeiXinMessage
     * @throws WeiXinException
     */
    public WeiXinMessage parse(WeiXinSession session, HttpServletRequest request) throws WeiXinException;

    /**
     * 发送消息
     *
     * @param message WeiXinMessage
     */
    public void sendMessage(WeiXinMessage message);

}
