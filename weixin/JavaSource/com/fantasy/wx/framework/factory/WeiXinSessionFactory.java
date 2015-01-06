package com.fantasy.wx.framework.factory;

import com.fantasy.wx.framework.core.WeiXinCoreHelper;
import com.fantasy.wx.framework.account.AccountDetailsService;
import com.fantasy.wx.framework.exception.WeiXinException;
import com.fantasy.wx.framework.message.WeiXinMessage;
import com.fantasy.wx.framework.session.WeiXinSession;

public interface WeiXinSessionFactory {

    /**
     * 第三方工具类
     *
     * @return Signature
     */
    public WeiXinCoreHelper getWeiXinCoreHelper();

    /**
     * 获取当前的 WeiXinSession
     *
     * @return WeiXinSession
     * @throws WeiXinException
     */
    public WeiXinSession getCurrentSession() throws WeiXinException;

    /**
     * 返回一个 WeiXinSession 对象，如果当前不存在，则创建一个新的session对象
     *
     * @return WeiXinSession
     * @throws WeiXinException
     */
    public WeiXinSession openSession(String appid) throws WeiXinException;

    /**
     * 获取微信账号存储服务
     *
     * @return AccountDetailsService
     */
    public AccountDetailsService getAccountDetailsService();

    /**
     * 处理接收到的请求
     *
     * @param message http response
     * @return WeiXinMessage
     */
    public WeiXinMessage<?> execute(WeiXinMessage message) throws WeiXinException;

}
