package org.jfantasy.pay.event;

import org.jfantasy.pay.bean.Point;
import org.springframework.context.ApplicationEvent;

public class PointChangedEvent extends ApplicationEvent {

    public PointChangedEvent(Point point) {
        super(point);
    }

    public Point getPoint() {
        return (Point) this.getSource();
    }

}