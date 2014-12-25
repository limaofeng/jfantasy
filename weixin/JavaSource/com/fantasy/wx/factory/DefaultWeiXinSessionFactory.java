package com.fantasy.wx.factory;

import com.fantasy.wx.WeiXinCoreHelper;
import com.fantasy.wx.account.AccountDetailsService;
import com.fantasy.wx.exception.WeiXinException;
import com.fantasy.wx.session.DefaultWeiXinSession;
import com.fantasy.wx.session.WeiXinSession;

public class DefaultWeiXinSessionFactory implements WeiXinSessionFactory {

    private WeiXinCoreHelper weiXinCoreHelper;

    private AccountDetailsService accountDetailsService;

    /**
     * 当前 session 对象
     */
    private static ThreadLocal<WeiXinSession> current = new ThreadLocal<WeiXinSession>();

    private Class<? extends WeiXinSession> sessionClass = DefaultWeiXinSession.class;

    @Override
    public WeiXinCoreHelper getWeiXinCoreHelper() {
        return this.weiXinCoreHelper;
    }

    @Override
    public WeiXinSession getCurrentSession() throws WeiXinException {
        if (current.get() == null) {
            throw new WeiXinException("未初始化 WeiXinSession 对象");
        }
        return current.get();
    }

    @Override
    public WeiXinSession openSession(String appid) throws WeiXinException {
        if (current.get() != null && appid.equals(current.get().getId())) {
            return getCurrentSession();
        }
        //创建新的session对象
        current.set(new DefaultWeiXinSession(this.accountDetailsService.loadAccountByAppid(appid)));
        return current.get();
    }

    @Override
    public AccountDetailsService getAccountDetailsService() {
        return this.accountDetailsService;
    }

    public Class<? extends WeiXinSession> getSessionClass() {
        return sessionClass;
    }

    public void setSessionClass(Class<? extends WeiXinSession> sessionClass) {
        this.sessionClass = sessionClass;
    }

    public void setAccountDetailsService(AccountDetailsService accountDetailsService) {
        this.accountDetailsService = accountDetailsService;
    }

    public void setWeiXinCoreHelper(WeiXinCoreHelper weiXinCoreHelper) {
        this.weiXinCoreHelper = weiXinCoreHelper;
    }
}
