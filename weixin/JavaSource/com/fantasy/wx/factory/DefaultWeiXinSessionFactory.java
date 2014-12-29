package com.fantasy.wx.factory;

import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.wx.account.AccountDetailsService;
import com.fantasy.wx.core.WeiXinCoreHelper;
import com.fantasy.wx.event.*;
import com.fantasy.wx.exception.NoSessionException;
import com.fantasy.wx.exception.WeiXinException;
import com.fantasy.wx.handler.WeiXinHandler;
import com.fantasy.wx.intercept.DefaultInvocation;
import com.fantasy.wx.intercept.Invocation;
import com.fantasy.wx.intercept.WeiXinMessageInterceptor;
import com.fantasy.wx.message.EventMessage;
import com.fantasy.wx.message.WeiXinMessage;
import com.fantasy.wx.message.content.Event;
import com.fantasy.wx.message.content.EventLocation;
import com.fantasy.wx.session.DefaultWeiXinSession;
import com.fantasy.wx.session.WeiXinSession;

import java.util.*;

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

    @Override
    public WeiXinSession getCurrentSession() throws WeiXinException {
        return WeiXinSessionUtils.getCurrentSession();
    }

    @Override
    public WeiXinSession openSession(String appid) throws WeiXinException {
        try {
            return WeiXinSessionUtils.getCurrentSession();
        } catch (NoSessionException e) {
            return WeiXinSessionUtils.saveSession(new DefaultWeiXinSession(this.accountDetailsService.loadAccountByAppid(appid),weiXinCoreHelper));
        }
    }

    @Override
    public AccountDetailsService getAccountDetailsService() {
        return this.accountDetailsService;
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
                                ((ClickEventListener) listener).onClick(session, (Event) message.getContent());
                            } else if (listener instanceof LocationEventListener) {
                                ((LocationEventListener) listener).onLocation(session, (EventLocation) message.getContent());
                            } else if (listener instanceof ScanEventListener) {
                                ((ScanEventListener) listener).onScan(session, (Event) message.getContent());
                            } else if (listener instanceof SubscribeEventListener) {
                                ((SubscribeEventListener) listener).onSubscribe(session, (Event) message.getContent());
                            } else if (listener instanceof UnsubscribeEventListener) {
                                ((UnsubscribeEventListener) listener).onUnsubscribe(session, (Event) message.getContent());
                            } else if (listener instanceof ViewEventListener) {
                                ((ViewEventListener) listener).onView(session, (Event) message.getContent());
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
