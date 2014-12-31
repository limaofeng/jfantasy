package com.fantasy.wx.factory;

import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.wx.account.AccountDetailsService;
import com.fantasy.wx.core.WeiXinCoreHelper;
import com.fantasy.wx.event.WeiXinEventListener;
import com.fantasy.wx.handler.DefaultEventHandler;
import com.fantasy.wx.handler.NotReplyTextHandler;
import com.fantasy.wx.handler.WeiXinHandler;
import com.fantasy.wx.intercept.LogInterceptor;
import com.fantasy.wx.intercept.WeiXinMessageInterceptor;
import com.fantasy.wx.message.EventMessage;
import com.fantasy.wx.session.AccountDetails;
import com.fantasy.wx.session.DefaultWeiXinSession;
import com.fantasy.wx.session.WeiXinSession;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WeiXinSessionFactoryBean implements FactoryBean<WeiXinSessionFactory>, InitializingBean {

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

    @Override
    public void afterPropertiesSet() throws Exception {
        DefaultWeiXinSessionFactory factory = new DefaultWeiXinSessionFactory();

        if (this.accountDetailsService == null) {
            factory.setAccountDetailsService(SpringContextUtil.getBeanByType(AccountDetailsService.class));
        }
        if (this.weiXinCoreHelper == null) {
            factory.setWeiXinCoreHelper(SpringContextUtil.getBeanByType(WeiXinCoreHelper.class));
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

    }

    @Override
    public WeiXinSessionFactory getObject() throws Exception {
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
