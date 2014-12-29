package com.fantasy.wx.core;

import com.fantasy.wx.exception.WeiXinException;
import com.fantasy.wx.message.WeiXinMessage;
import com.fantasy.wx.session.AccountDetails;

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
     * @param request HTTP请求
     * @return WeiXinMessage
     * @throws WeiXinException
     */
    public WeiXinMessage parseInMessage(HttpServletRequest request) throws WeiXinException;

    /**
     * 构建回复的消息
     *
     * @param encryptType encryptType
     * @param message     消息
     * @return String
     * @throws WeiXinException
     */
    public String buildOutMessage(String encryptType, WeiXinMessage message) throws WeiXinException;

    /**
     * 发送消息
     *
     * @param message WeiXinMessage
     */
    public void sendMessage(WeiXinMessage message);

}
