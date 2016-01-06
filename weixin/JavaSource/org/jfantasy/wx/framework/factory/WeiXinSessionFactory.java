package org.jfantasy.wx.framework.factory;

import org.jfantasy.wx.framework.core.WeiXinCoreHelper;
import org.jfantasy.wx.framework.account.AccountDetailsService;
import org.jfantasy.wx.framework.exception.WeiXinException;
import org.jfantasy.wx.framework.message.WeiXinMessage;
import org.jfantasy.wx.framework.session.WeiXinSession;

public interface WeiXinSessionFactory {

    /**
     * 第三方工具类
     *
     * @return Signature
     */
    WeiXinCoreHelper getWeiXinCoreHelper();

    /**
     * 获取当前的 WeiXinSession
     *
     * @return WeiXinSession
     * @throws WeiXinException
     */
    WeiXinSession getCurrentSession() throws WeiXinException;

    /**
     * 返回一个 WeiXinSession 对象，如果当前不存在，则创建一个新的session对象
     *
     * @return WeiXinSession
     * @throws WeiXinException
     */
    WeiXinSession openSession(String appid) throws WeiXinException;

    /**
     * 获取微信账号存储服务
     *
     * @return AccountDetailsService
     */
    AccountDetailsService getAccountDetailsService();

    /**
     * 处理接收到的请求
     *
     * @param message http response
     * @return WeiXinMessage
     */
    WeiXinMessage<?> execute(WeiXinMessage message) throws WeiXinException;

}
