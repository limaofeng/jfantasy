package com.fantasy.wx.session;

import com.fantasy.wx.core.WeiXinCoreHelper;

/**
 * 微信Session默认实现
 */
public class DefaultWeiXinSession extends AbstractWeiXinSession {

    public DefaultWeiXinSession(AccountDetails accountDetails, WeiXinCoreHelper weiXinCoreHelper) {
        super(accountDetails,weiXinCoreHelper);
    }

}
