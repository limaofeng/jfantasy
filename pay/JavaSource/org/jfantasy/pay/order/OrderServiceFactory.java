package org.jfantasy.pay.order;

import org.apache.log4j.Logger;
import org.jfantasy.framework.spring.mvc.error.NotFoundException;
import org.jfantasy.pay.OrderServiceBuilder;
import org.jfantasy.pay.builder.HttpOrderServiceBuilder;
import org.jfantasy.pay.builder.RpcOrderServiceBuilder;
import org.jfantasy.pay.order.entity.enums.CallType;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrderServiceFactory implements InitializingBean, ApplicationContextAware {

    private static final Logger LOGGER = Logger.getLogger(OrderServiceFactory.class);

    private Map<String, OrderService> orderServiceMap;

    private ApplicationContext applicationContext;

    private Map<CallType, OrderServiceBuilder> builders = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        OrderServiceBuilder builder = applicationContext.getBean(RpcOrderServiceBuilder.class);
        builders.put(builder.getCallType(), builder);
        builder = applicationContext.getBean(HttpOrderServiceBuilder.class);
        builders.put(builder.getCallType(), builder);
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

    public void unregister(String type) {
        this.orderServiceMap.remove(type);
    }

    public OrderServiceBuilder getBuilder(CallType callType) {
        return builders.get(callType);
    }

}
