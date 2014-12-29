package com.fantasy.wx.message;


import com.fantasy.wx.message.content.Event;

import java.util.Date;

public class DefalutEventMessage extends AbstractWeiXinMessage<Event> implements EventMessage<Event> {

    public DefalutEventMessage(Long id, String fromUserName, Date createTime) {
        super(id, fromUserName, createTime);
    }

    @Override
    public EventType getEventType() {
        return this.getContent().getType();
    }

}
