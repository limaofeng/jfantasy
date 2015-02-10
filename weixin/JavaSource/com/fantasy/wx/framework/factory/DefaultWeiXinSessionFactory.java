package com.fantasy.wx.framework.factory;

import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.wx.framework.account.AccountDetailsService;
import com.fantasy.wx.framework.core.WeiXinCoreHelper;
import com.fantasy.wx.framework.event.*;
import com.fantasy.wx.framework.exception.NoSessionException;
import com.fantasy.wx.framework.exception.WeiXinException;
import com.fantasy.wx.framework.handler.WeiXinHandler;
import com.fantasy.wx.framework.intercept.DefaultInvocation;
import com.fantasy.wx.framework.intercept.Invocation;
import com.fantasy.wx.framework.intercept.WeiXinMessageInterceptor;
import com.fantasy.wx.framework.message.EventMessage;
import com.fantasy.wx.framework.message.WeiXinMessage;
import com.fantasy.wx.framework.message.content.Event;
import com.fantasy.wx.framework.message.content.EventLocation;
import com.fantasy.wx.framework.session.DefaultWeiXinSession;
import com.fantasy.wx.framework.session.WeiXinSession;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DefaultWeiXinSessionFactory implements WeiXinSessionFactory {

    private WeiXinCoreHelper weiXinCoreHelper;

    private AccountDetailsService accountDetailsService;

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

    @Override
    public WeiXinSession getCurrentSession() throws WeiXinException {
        return WeiXinSessionUtils.getCurrentSession();
    }

    @Override
    public WeiXinSession openSession(String appid) throws WeiXinException {
        try {
            return WeiXinSessionUtils.getCurrentSession();
        } catch (NoSessionException e) {
            weiXinSessions.putIfAbsent(appid, new DefaultWeiXinSession(this.accountDetailsService.loadAccountByAppid(appid), weiXinCoreHelper));
            return WeiXinSessionUtils.saveSession(weiXinSessions.get(appid));
        }
    }

    @Override
    public AccountDetailsService getAccountDetailsService() {
        return this.accountDetailsService;
    }

    public void sendMessage(WeiXinMessage message, String userId) {

    }

    @Override
    public WeiXinMessage<?> execute(WeiXinMessage message) throws WeiXinException {
        List<Object> handler = new ArrayList<Object>(weiXinMessageInterceptors);
        if (message instanceof EventMessage) {
            final List<WeiXinEventListener> listeners = (List<WeiXinEventListener>) ObjectUtil.defaultValue(eventListeners.get(((EventMessage) message).getEventType()), Collections.emptyList());
            handler.add(new WeiXinMessageInterceptor() {
                @Override
                public WeiXinMessage intercept(WeiXinSession session, WeiXinMessage message, Invocation invocation) throws WeiXinException {
                    try {
                        return invocation.invoke();
                    } finally {
                        for (WeiXinEventListener listener : listeners) {
                            if (listener instanceof ClickEventListener) {
                                ((ClickEventListener) listener).onClick(session, (Event) message.getContent(), (EventMessage) message);
                            } else if (listener instanceof LocationEventListener) {
                                ((LocationEventListener) listener).onLocation(session, (EventLocation) message.getContent(), (EventMessage) message);
                            } else if (listener instanceof ScanEventListener) {
                                ((ScanEventListener) listener).onScan(session, (Event) message.getContent(), (EventMessage) message);
                            } else if (listener instanceof SubscribeEventListener) {
                                ((SubscribeEventListener) listener).onSubscribe(session, (Event) message.getContent(), (EventMessage) message);
                            } else if (listener instanceof UnsubscribeEventListener) {
                                ((UnsubscribeEventListener) listener).onUnsubscribe(session, (Event) message.getContent(), (EventMessage) message);
                            } else if (listener instanceof ViewEventListener) {
                                ((ViewEventListener) listener).onView(session, (Event) message.getContent(), (EventMessage) message);
                            }
                        }
                    }
                }
            });
            handler.add(this.eventHandler);
        } else {
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
