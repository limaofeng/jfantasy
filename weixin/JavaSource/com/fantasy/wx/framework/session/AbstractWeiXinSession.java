package com.fantasy.wx.framework.session;

import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.wx.framework.core.WeiXinCoreHelper;
import com.fantasy.wx.framework.exception.WeiXinException;
import com.fantasy.wx.framework.message.content.*;
import com.fantasy.wx.framework.message.user.Group;
import com.fantasy.wx.framework.message.user.User;
import com.fantasy.wx.framework.oauth2.Scope;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 微信 session 抽象实现
 */
public abstract class AbstractWeiXinSession implements WeiXinSession {

    private final Log LOG = LogFactory.getLog(this.getClass());

    private String id;

    //缓存所有group信息
    private List<Group> groups = new ArrayList<Group>();
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
    public void sendVoiceMessage(final Voice content, final String... toUsers) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    AbstractWeiXinSession.this.weiXinCoreHelper.sendVoiceMessage(AbstractWeiXinSession.this, content, toUsers);
                } catch (WeiXinException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        });
    }

    @Override
    public void sendVoiceMessage(final Voice content, final long toGroup) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    AbstractWeiXinSession.this.weiXinCoreHelper.sendVoiceMessage(AbstractWeiXinSession.this, content, toGroup);
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
    public void sendNewsMessage(final List<Article> content, final String... toUsers) {
        if(content.isEmpty()){
            return;
        }
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
    public void sendNewsMessage(final News content, final String toUser) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    AbstractWeiXinSession.this.weiXinCoreHelper.sendNewsMessage(AbstractWeiXinSession.this, content, toUser);
                } catch (WeiXinException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        });
    }

    @Override
    public void sendNewsMessage(final List<Article> content, final long toGroup) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    AbstractWeiXinSession.this.weiXinCoreHelper.sendNewsMessage(AbstractWeiXinSession.this,content,toGroup);
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
    public void sendTextMessage(final String content, final long toGroup) {
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
    public String getAuthorizationUrl(String redirectUri, Scope scope) {
        try {
            return this.weiXinCoreHelper.oauth2buildAuthorizationUrl(this, redirectUri, scope, "");
        } catch (WeiXinException e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public String getAuthorizationUrl(String redirectUri, Scope scope, String state) {
        try {
            return this.weiXinCoreHelper.oauth2buildAuthorizationUrl(this, redirectUri, scope, state);
        } catch (WeiXinException e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public User getAuthorizedUser(String code) {
        try {
            return this.weiXinCoreHelper.getUser(this, this.weiXinCoreHelper.oauth2getAccessToken(this, code));
        } catch (WeiXinException e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public User getUser(String userId) {
        try {
            return this.weiXinCoreHelper.getUser(this, userId);
        } catch (WeiXinException e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public List<User> getUsers() {
        try {
            return this.weiXinCoreHelper.getUsers(this);
        } catch (WeiXinException e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public List<Group> getGroups() {
        try {
            if (!this.groups.isEmpty()) {
                return this.groups;
            }
            return this.groups = this.weiXinCoreHelper.getGroups(this);
        } catch (WeiXinException e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Group createGroup(String name) {
        try {
            Group group = this.weiXinCoreHelper.groupCreate(this, name);
            this.groups.add(group);
            return group;
        } catch (WeiXinException e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Group updateGroup(long groupId, String name) {
        try {
            this.weiXinCoreHelper.groupUpdate(this, groupId, name);
            Group group = ObjectUtil.find(getGroups(), "getId()", groupId);
            group.setName(name);
            return group;
        } catch (WeiXinException e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void moveUser(String userId, long groupId) {
        try {
            long _groupId = this.weiXinCoreHelper.getGroupIdByUserId(this, userId);
            ObjectUtil.remove(ObjectUtil.find(getGroups(), "getId()", _groupId).getUsers(), "getOpenId()", "userId");
            this.weiXinCoreHelper.userUpdateGroup(this, userId, groupId);
            Group group = ObjectUtil.find(getGroups(), "getId()", groupId);
            group.addUser(getUser(userId));
        } catch (WeiXinException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public AccountDetails getAccountDetails() {
        return this.accountDetails;
    }

}
