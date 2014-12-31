package com.fantasy.wx.session;

import com.fantasy.wx.core.WeiXinCoreHelper;
import com.fantasy.wx.exception.WeiXinException;
import com.fantasy.wx.message.content.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 微信 session 抽象实现
 */
public abstract class AbstractWeiXinSession implements WeiXinSession {

    private final Log LOG = LogFactory.getLog(this.getClass());

    private String id;

    private AccountDetails accountDetails;
    private WeiXinCoreHelper weiXinCoreHelper;
    private final static ExecutorService executor = Executors.newSingleThreadScheduledExecutor();

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
    public void sendImageMessage(final Image content, final String toUser) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    AbstractWeiXinSession.this.weiXinCoreHelper.sendImageMessage(AbstractWeiXinSession.this, content, toUser);
                } catch (WeiXinException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        });
    }

    @Override
    public void sendVoiceMessage(final Voice content, final String toUser) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    AbstractWeiXinSession.this.weiXinCoreHelper.sendVoiceMessage(AbstractWeiXinSession.this, content, toUser);
                } catch (WeiXinException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        });
    }

    @Override
    public void sendVideoMessage(final Video content, final String toUser) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    AbstractWeiXinSession.this.weiXinCoreHelper.sendVideoMessage(AbstractWeiXinSession.this, content, toUser);
                } catch (WeiXinException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        });
    }

    @Override
    public void sendMusicMessage(final Music content, final String toUser) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    AbstractWeiXinSession.this.weiXinCoreHelper.sendMusicMessage(AbstractWeiXinSession.this, content, toUser);
                } catch (WeiXinException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        });
    }

    @Override
    public void sendNewsMessage(final News content, final String... toUsers) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    AbstractWeiXinSession.this.weiXinCoreHelper.sendNewsMessage(AbstractWeiXinSession.this, content, toUsers);
                } catch (WeiXinException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        });
    }

    @Override
    public void sendTextMessage(final String content, final String... toUsers) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    AbstractWeiXinSession.this.weiXinCoreHelper.sendTextMessage(AbstractWeiXinSession.this, content, toUsers);
                } catch (WeiXinException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        });
    }

    @Override
    public void sendTextMessage(final String content, final Long toGroup) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    AbstractWeiXinSession.this.weiXinCoreHelper.sendTextMessage(AbstractWeiXinSession.this, content, toGroup);
                } catch (WeiXinException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        });
    }

    @Override
    public AccountDetails getAccountDetails() {
        return this.accountDetails;
    }

    @Override
    public WeiXinCoreHelper getWeiXinCoreHelper() {
        return weiXinCoreHelper;
    }

}
