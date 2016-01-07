package org.jfantasy.wx.framework.event;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfantasy.wx.framework.message.EventMessage;
import org.jfantasy.wx.framework.message.content.Event;
import org.jfantasy.wx.framework.message.content.EventLocation;
import org.jfantasy.wx.framework.session.WeiXinSession;

import java.util.ArrayList;
import java.util.List;

public class EventListenerAdapter {

    private static final Log LOG = LogFactory.getLog(EventListenerAdapter.class);

    private List<WeiXinEventListener> listeners;
    private EventMessage.EventType eventType;
    private List<EventListenerAdapter> adapters = new ArrayList<EventListenerAdapter>();

    public EventListenerAdapter(EventMessage.EventType eventType, List<WeiXinEventListener> listeners) {
        this.eventType = eventType;
    }


    public void execute(WeiXinSession session, Event content, EventMessage message) {
        for (WeiXinEventListener listener : listeners) {
            if (listener instanceof ClickEventListener && eventType == EventMessage.EventType.CLICK) {
                ((ClickEventListener) listener).onClick(session, content, message);
            } else if (listener instanceof LocationEventListener && eventType == EventMessage.EventType.location) {
                ((LocationEventListener) listener).onLocation(session, (EventLocation) content, message);
            } else if (listener instanceof ScanEventListener && eventType == EventMessage.EventType.SCAN) {
                ((ScanEventListener) listener).onScan(session, content, message);
            } else if (listener instanceof SubscribeEventListener && eventType == EventMessage.EventType.subscribe) {
                ((SubscribeEventListener) listener).onSubscribe(session, content, message);
            } else if (listener instanceof UnsubscribeEventListener && eventType == EventMessage.EventType.unsubscribe) {
                ((UnsubscribeEventListener) listener).onUnsubscribe(session, content, message);
            } else if (listener instanceof ViewEventListener && eventType == EventMessage.EventType.VIEW) {
                ((ViewEventListener) listener).onView(session, content, message);
            } else {
                LOG.error("监听器的与监听类型不匹配:" + eventType + "=" + listener);
            }
        }
    }

}
