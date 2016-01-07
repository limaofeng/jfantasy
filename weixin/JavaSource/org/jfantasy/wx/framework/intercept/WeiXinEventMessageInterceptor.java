package org.jfantasy.wx.framework.intercept;

import org.jfantasy.wx.framework.event.EventListenerAdapter;
import org.jfantasy.wx.framework.event.WeiXinEventListener;
import org.jfantasy.wx.framework.exception.WeiXinException;
import org.jfantasy.wx.framework.message.EventMessage;
import org.jfantasy.wx.framework.message.WeiXinMessage;
import org.jfantasy.wx.framework.message.content.Event;
import org.jfantasy.wx.framework.session.WeiXinSession;

import java.util.List;

public class WeiXinEventMessageInterceptor implements WeiXinMessageInterceptor {

    private EventListenerAdapter adapter;


    public WeiXinEventMessageInterceptor(EventMessage.EventType eventType, List<WeiXinEventListener> listeners) {
        this.adapter = new EventListenerAdapter(eventType, listeners);
    }

    @Override
    public WeiXinMessage intercept(WeiXinSession session, WeiXinMessage message, Invocation invocation) throws WeiXinException {
        try {
            return invocation.invoke();
        } finally {
            adapter.execute(session, (Event) message.getContent(), (EventMessage) message);
        }
    }

}
