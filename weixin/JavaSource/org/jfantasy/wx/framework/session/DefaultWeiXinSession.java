package org.jfantasy.wx.framework.session;

import org.jfantasy.wx.framework.core.WeiXinCoreHelper;

/**
 * 微信Session默认实现
 */
public class DefaultWeiXinSession extends AbstractWeiXinSession {

    public DefaultWeiXinSession(AccountDetails accountDetails, WeiXinCoreHelper weiXinCoreHelper) {
        super(accountDetails,weiXinCoreHelper);
    }

}
