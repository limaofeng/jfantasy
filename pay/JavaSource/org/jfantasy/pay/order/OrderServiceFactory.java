package org.jfantasy.pay.order;

import org.apache.log4j.Logger;
import org.jfantasy.framework.spring.mvc.error.NotFoundException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrderServiceFactory implements ApplicationContextAware {

    private static final Logger LOGGER = Logger.getLogger(OrderServiceFactory.class);

    private Map<String, OrderService> orderServiceMap;

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public OrderServiceFactory() {
        orderServiceMap = new HashMap<String, OrderService>();
    }

    public OrderServiceFactory(Map<String, OrderService> orderDetailsServices) {
        this.orderServiceMap = orderDetailsServices;
    }

    public void register(String type, OrderService orderService) {
        if (orderServiceMap.containsKey(type)) {
            LOGGER.warn("type = " + type + "的 OrderService 已经存在,这将覆盖原有的 OrderService ");
        }
        orderServiceMap.put(type.toLowerCase(), orderService);
    }

    public void register(String[] types, OrderService orderService) {
        for (String type : types) {
            register(type, orderService);
        }
    }

    public OrderService getOrderService(String type) {
        if (!this.orderServiceMap.containsKey(type.toLowerCase())) {
            throw new NotFoundException("orderType[" + type + "] 对应的 PaymentOrderService 未配置！");
        }
        return orderServiceMap.get(type.toLowerCase());
    }

    public boolean containsType(String type) {
        return this.orderServiceMap.containsKey(type.toLowerCase());
    }
}
