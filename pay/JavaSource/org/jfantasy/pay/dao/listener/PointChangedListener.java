package org.jfantasy.pay.dao.listener;

import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostUpdateEvent;
import org.jfantasy.framework.dao.hibernate.listener.AbstractChangedListener;
import org.jfantasy.pay.bean.Point;
import org.jfantasy.pay.event.PointChangedEvent;


public class PointChangedListener extends AbstractChangedListener<Point> {

    @Override
    public void onPostInsert(Point point,PostInsertEvent event) {
        if (missing(event)) {
            return;
        }
        getApplicationContext().publishEvent(new PointChangedEvent(point));
    }

    @Override
    public void onPostUpdate(Point point,PostUpdateEvent event) {
        if (modify(event, "status")) {
            getApplicationContext().publishEvent(new PointChangedEvent(point));
        }
    }

}
