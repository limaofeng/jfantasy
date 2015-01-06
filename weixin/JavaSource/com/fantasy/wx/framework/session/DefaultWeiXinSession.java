package com.fantasy.wx.framework.session;

import com.fantasy.wx.framework.core.WeiXinCoreHelper;

/**
 * 微信Session默认实现
 */
public class DefaultWeiXinSession extends AbstractWeiXinSession {

    public DefaultWeiXinSession(AccountDetails accountDetails, WeiXinCoreHelper weiXinCoreHelper) {
        super(accountDetails,weiXinCoreHelper);
    }

}
