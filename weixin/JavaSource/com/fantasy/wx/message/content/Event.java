package com.fantasy.wx.message.content;

import com.fantasy.wx.message.EventMessage;

public class Event {
    /**
     * 事件类型
     */
    private EventMessage.EventType type;
    /**
     * 事件KEY值，与自定义菜单接口中KEY值对应
     */
    private String key;
    /**
     * 二维码的ticket，可用来换取二维码图片
     */
    private String ticket;

    public Event(String event) {
        this.type = EventMessage.EventType.valueOf(event);
    }

    public Event(String event, String eventKey, String ticket) {
        this(event, eventKey);
        this.ticket = ticket;
    }

    public Event(String event, String eventKey) {
        this(event);
        this.key = eventKey;
    }

    public EventMessage.EventType getType() {
        return type;
    }

    public void setType(EventMessage.EventType type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    @Override
    public String toString() {
        return "Event{" +
                "type=" + type +
                ", key='" + key + '\'' +
                ", ticket='" + ticket + '\'' +
                '}';
    }
}
