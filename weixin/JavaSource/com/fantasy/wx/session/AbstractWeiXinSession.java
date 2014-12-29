package com.fantasy.wx.session;

import com.fantasy.wx.message.WeiXinMessage;

import java.io.IOException;

/**
 * 微信 session 抽象实现
 */
public abstract class AbstractWeiXinSession implements WeiXinSession {

    private String id;

    private AccountDetails accountDetails;

    public AbstractWeiXinSession(AccountDetails accountDetails) {
        this.accountDetails = accountDetails;
        this.id = accountDetails.getAppId();
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void sendMessage(WeiXinMessage<?> message) throws IOException {

    }

    @Override
    public AccountDetails getAccountDetails() {
        return this.accountDetails;
    }
}
