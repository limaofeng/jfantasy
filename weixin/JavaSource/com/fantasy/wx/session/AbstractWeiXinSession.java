package com.fantasy.wx.session;

import com.fantasy.wx.core.WeiXinCoreHelper;
import com.fantasy.wx.exception.WeiXinException;
import com.fantasy.wx.message.content.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 微信 session 抽象实现
 */
public abstract class AbstractWeiXinSession implements WeiXinSession {

    private final Log LOG = LogFactory.getLog(this.getClass());

    private String id;

    private AccountDetails accountDetails;
    private WeiXinCoreHelper weiXinCoreHelper;

    public AbstractWeiXinSession(AccountDetails accountDetails, WeiXinCoreHelper weiXinCoreHelper) {
        this.accountDetails = accountDetails;
        this.id = accountDetails.getAppId();
        this.weiXinCoreHelper = weiXinCoreHelper;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void sendImageMessage(Image content, String toUser) {
        try {
            weiXinCoreHelper.sendImageMessage(this, content, toUser);
        } catch (WeiXinException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public void sendVoiceMessage(Voice content, String toUser) {
        try {
            weiXinCoreHelper.sendVoiceMessage(this, content, toUser);
        } catch (WeiXinException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public void sendVideoMessage(Video content, String toUser) {
        try {
            weiXinCoreHelper.sendVideoMessage(this, content, toUser);
        } catch (WeiXinException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public void sendMusicMessage(Music content, String toUser) {
        try {
            weiXinCoreHelper.sendMusicMessage(this, content, toUser);
        } catch (WeiXinException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public void sendNewsMessage(News content, String toUser) {
        try {
            weiXinCoreHelper.sendNewsMessage(this, content, toUser);
        } catch (WeiXinException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public void sendTextMessage(String content, String toUser) {
        try {
            weiXinCoreHelper.sendTextMessage(this, content, toUser);
        } catch (WeiXinException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public AccountDetails getAccountDetails() {
        return this.accountDetails;
    }


}
