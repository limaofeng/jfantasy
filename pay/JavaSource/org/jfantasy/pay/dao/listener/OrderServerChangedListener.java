package org.jfantasy.pay.dao.listener;

import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostUpdateEvent;
import org.jfantasy.framework.dao.annotations.EventListener;
import org.jfantasy.framework.dao.hibernate.listener.AbstractChangedListener;
import org.jfantasy.framework.spring.SpringContextUtil;
import org.jfantasy.pay.bean.OrderServer;
import org.jfantasy.pay.order.OrderServiceFactory;
import org.springframework.stereotype.Component;

@Component
@EventListener
public class OrderServerChangedListener extends AbstractChangedListener<OrderServer> {

    private static final long serialVersionUID = 6697184909285106945L;
    private OrderServiceFactory orderServiceFactory;

    private OrderServiceFactory orderServerService() {
        if (orderServiceFactory == null) {
            return orderServiceFactory = SpringContextUtil.getBeanByType(OrderServiceFactory.class);
        }
        return orderServiceFactory;
    }

    @Override
    public void onPostInsert(OrderServer entity, PostInsertEvent event) {
        if (entity.isEnabled()) {
            orderServerService().register(entity.getType(), orderServiceFactory.getBuilder(entity.getCallType()).build(entity.getProperties()));
        }
    }

    @Override
    public void onPostUpdate(OrderServer entity, PostUpdateEvent event) {
        if (modify(event, "enabled")) {
            if (entity.isEnabled()) {
                orderServerService().register(entity.getType(), orderServiceFactory.getBuilder(entity.getCallType()).build(entity.getProperties()));
            } else {
                orderServerService().unregister(entity.getType());
            }
        }
    }


}
