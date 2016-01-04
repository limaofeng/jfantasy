package org.jfantasy.wx.framework.factory;

import org.jfantasy.framework.spring.SpringContextUtil;
import org.jfantasy.wx.framework.account.AccountDetailsService;
import org.jfantasy.wx.framework.core.MpCoreHelper;
import org.jfantasy.wx.framework.core.WeiXinCoreHelper;
import org.jfantasy.wx.framework.event.WeiXinEventListener;
import org.jfantasy.wx.framework.handler.DefaultEventHandler;
import org.jfantasy.wx.framework.handler.NotReplyTextHandler;
import org.jfantasy.wx.framework.handler.WeiXinHandler;
import org.jfantasy.wx.framework.intercept.LogInterceptor;
import org.jfantasy.wx.framework.intercept.WeiXinMessageInterceptor;
import org.jfantasy.wx.framework.message.EventMessage;
import org.jfantasy.wx.framework.session.AccountDetails;
import org.jfantasy.wx.framework.session.DefaultWeiXinSession;
import org.jfantasy.wx.framework.session.WeiXinSession;
import org.jfantasy.wx.service.AccountService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.FactoryBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WeiXinSessionFactoryBean implements FactoryBean<WeiXinSessionFactory> {

    private static final Log LOG = LogFactory.getLog(WeiXinSessionFactoryBean.class);

    /**
     * 微信session工厂
     */
    private WeiXinSessionFactory weiXinSessionFactory;

    private WeiXinCoreHelper weiXinCoreHelper;

    private AccountDetailsService accountDetailsService;

    private Class<? extends WeiXinSession> sessionClass = DefaultWeiXinSession.class;

    private WeiXinHandler messageHandler = new NotReplyTextHandler();

    private WeiXinHandler eventHandler = new DefaultEventHandler();

    private Map<EventMessage.EventType, List<WeiXinEventListener>> eventListeners = new HashMap<EventMessage.EventType, List<WeiXinEventListener>>();

    private List<WeiXinMessageInterceptor> weiXinMessageInterceptors = new ArrayList<WeiXinMessageInterceptor>() {
        {
            this.add(new LogInterceptor());
        }
    };

    public void afterPropertiesSet() throws Exception {
        long start = System.currentTimeMillis();

        DefaultWeiXinSessionFactory factory = new DefaultWeiXinSessionFactory();

        if (this.accountDetailsService == null) {
            this.accountDetailsService = SpringContextUtil.getBeanByType(AccountService.class);
        }
        factory.setAccountDetailsService(this.accountDetailsService);

        if (this.weiXinCoreHelper != null) {
            factory.setWeiXinCoreHelper(this.weiXinCoreHelper);
        } else {
            factory.setWeiXinCoreHelper(SpringContextUtil.getBeanByType(MpCoreHelper.class));
        }

        factory.setMessageHandler(this.messageHandler);
        factory.setEventHandler(this.eventHandler);
        factory.setSessionClass(sessionClass);
        factory.setWeiXinMessageInterceptors(weiXinMessageInterceptors);
        factory.setEventListeners(this.eventListeners);

        this.weiXinSessionFactory = factory;

        for (AccountDetails accountDetails : weiXinSessionFactory.getAccountDetailsService().getAll()) {
            weiXinSessionFactory.getWeiXinCoreHelper().register(accountDetails);
        }

        LOG.error("\n初始化 WeiXinSessionFactory 耗时:" + (System.currentTimeMillis() - start) + "ms");
    }

    @Override
    public WeiXinSessionFactory getObject() throws Exception {
        if(this.weiXinSessionFactory == null){
            afterPropertiesSet();
        }
        return weiXinSessionFactory;
    }

    @Override
    public Class<?> getObjectType() {
        return DefaultWeiXinSessionFactory.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setWeiXinCoreHelper(WeiXinCoreHelper weiXinCoreHelper) {
        this.weiXinCoreHelper = weiXinCoreHelper;
    }

    public void setWeiXinMessageInterceptors(List<WeiXinMessageInterceptor> weiXinMessageInterceptors) {
        this.weiXinMessageInterceptors = weiXinMessageInterceptors;
    }

    public void setMessageHandler(WeiXinHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    public void setEventHandler(WeiXinHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    public void setSessionClass(Class<? extends WeiXinSession> sessionClass) {
        this.sessionClass = sessionClass;
    }

    public void setAccountDetailsService(AccountDetailsService accountDetailsService) {
        this.accountDetailsService = accountDetailsService;
    }

    public void setEventListeners(Map<EventMessage.EventType, List<WeiXinEventListener>> eventListeners) {
        this.eventListeners = eventListeners;
    }
}
