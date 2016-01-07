package org.jfantasy.wx.framework.factory;

import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.wx.event.WeiXinEventMessageEvent;
import org.jfantasy.wx.event.WeiXinMessageEvent;
import org.jfantasy.wx.framework.account.AccountDetailsService;
import org.jfantasy.wx.framework.core.WeiXinCoreHelper;
import org.jfantasy.wx.framework.event.WeiXinEventListener;
import org.jfantasy.wx.framework.exception.WeiXinException;
import org.jfantasy.wx.framework.handler.WeiXinHandler;
import org.jfantasy.wx.framework.intercept.DefaultInvocation;
import org.jfantasy.wx.framework.intercept.WeiXinEventMessageInterceptor;
import org.jfantasy.wx.framework.intercept.WeiXinMessageInterceptor;
import org.jfantasy.wx.framework.message.EventMessage;
import org.jfantasy.wx.framework.message.WeiXinMessage;
import org.jfantasy.wx.framework.session.DefaultWeiXinSession;
import org.jfantasy.wx.framework.session.WeiXinSession;
import org.springframework.context.ApplicationContext;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DefaultWeiXinSessionFactory implements WeiXinSessionFactory {

    private WeiXinCoreHelper weiXinCoreHelper;

    private AccountDetailsService accountDetailsService;

    private ApplicationContext applicationContext;

    private Class<? extends WeiXinSession> sessionClass = DefaultWeiXinSession.class;

    private WeiXinHandler messageHandler;

    private WeiXinHandler eventHandler;

    private List<WeiXinMessageInterceptor> weiXinMessageInterceptors = new ArrayList<WeiXinMessageInterceptor>();

    private Map<EventMessage.EventType, List<WeiXinEventListener>> eventListeners = new HashMap<EventMessage.EventType, List<WeiXinEventListener>>();

    @Override
    public WeiXinCoreHelper getWeiXinCoreHelper() {
        return this.weiXinCoreHelper;
    }

    private ConcurrentMap<String, WeiXinSession> weiXinSessions = new ConcurrentHashMap<String, WeiXinSession>();

    public DefaultWeiXinSessionFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public WeiXinSession getCurrentSession() throws WeiXinException {
        return WeiXinSessionUtils.getCurrentSession();
    }

    @Override
    public WeiXinSession openSession(String appid) throws WeiXinException {
        if (!weiXinSessions.containsKey(appid)) {
            weiXinSessions.putIfAbsent(appid, new DefaultWeiXinSession(this.accountDetailsService.loadAccountByAppid(appid), weiXinCoreHelper));
        }
        return weiXinSessions.get(appid);
    }

    @Override
    public AccountDetailsService getAccountDetailsService() {
        return this.accountDetailsService;
    }

    @Override
    public WeiXinMessage<?> execute(WeiXinMessage message) throws WeiXinException {
        List<Object> handler = new ArrayList<Object>(weiXinMessageInterceptors);
        if (message instanceof EventMessage) {
            applicationContext.publishEvent(new WeiXinEventMessageEvent((EventMessage) message));//事件推送
            final List<WeiXinEventListener> listeners = ObjectUtil.defaultValue(eventListeners.get(((EventMessage) message).getEventType()), Collections.<WeiXinEventListener>emptyList());
            handler.add(new WeiXinEventMessageInterceptor(((EventMessage) message).getEventType(), listeners));//添加Event拦截器,用于触发事件
            handler.add(this.eventHandler);
        } else {
            applicationContext.publishEvent(new WeiXinMessageEvent(message));//消息事件推送
            handler.add(this.messageHandler);
        }
        return new DefaultInvocation(WeiXinSessionUtils.getCurrentSession(), message, handler.iterator()).invoke();
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

    public void setEventHandler(WeiXinHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    public void setMessageHandler(WeiXinHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    public void setWeiXinMessageInterceptors(List<WeiXinMessageInterceptor> weiXinMessageInterceptors) {
        this.weiXinMessageInterceptors = weiXinMessageInterceptors;
    }

    public void setEventListeners(Map<EventMessage.EventType, List<WeiXinEventListener>> eventListeners) {
        this.eventListeners = eventListeners;
    }

}
