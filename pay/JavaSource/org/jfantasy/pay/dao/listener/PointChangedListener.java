package org.jfantasy.pay.dao.listener;

import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.jfantasy.framework.dao.hibernate.listener.AbstractChangedListener;
import org.jfantasy.pay.bean.Point;
import org.jfantasy.pay.event.PointChangedEvent;


public class PointChangedListener extends AbstractChangedListener<Point> implements PostInsertEventListener, PostUpdateEventListener {

    @Override
    public void onPostInsert(PostInsertEvent event) {
        if (missing(event)) {
            return;
        }
        Point account = getEntity(event);
        getApplicationContext().publishEvent(new PointChangedEvent(account));
    }

    @Override
    public void onPostUpdate(PostUpdateEvent event) {
        if (missing(event)) {
            return;
        }
        Point account = getEntity(event);
        if (modify(event, "status")) {
            getApplicationContext().publishEvent(new PointChangedEvent(account));
        }
    }

}
