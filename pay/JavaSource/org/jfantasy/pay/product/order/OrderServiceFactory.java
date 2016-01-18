package org.jfantasy.pay.product.order;

import org.jfantasy.framework.spring.ClassPathScanner;
import org.jfantasy.framework.spring.mvc.error.NotFoundException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrderServiceFactory implements ApplicationContextAware, InitializingBean {

    private Map<String, OrderService> orderDetailsServices;

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private String[] packagesToScan = new String[]{"org.jfantasy.pay.service"};

    @Override
    public void afterPropertiesSet() throws Exception {
        for (String basePackage : packagesToScan) {
            for (Class<?> clazz : ClassPathScanner.getInstance().findInterfaceClasses(basePackage, OrderService.class)) {
                OrderService orderService = (OrderService) this.applicationContext.getBean(clazz);
                this.register(orderService.type(), orderService);
            }
        }
    }

    public OrderServiceFactory() {
        orderDetailsServices = new HashMap<String, OrderService>();
    }

    public OrderServiceFactory(Map<String, OrderService> orderDetailsServices) {
        this.orderDetailsServices = orderDetailsServices;
    }

    public void register(String type, OrderService orderDetailsService) {
        orderDetailsServices.put(type.toLowerCase(), orderDetailsService);
    }

    public OrderService getOrderService(String type) {
        if (!this.orderDetailsServices.containsKey(type.toLowerCase())) {
            throw new NotFoundException("orderType[" + type + "] 对应的 PaymentOrderService 未配置！");
        }
        return orderDetailsServices.get(type.toLowerCase());
    }

    public void setPackagesToScan(String[] packagesToScan) {
        this.packagesToScan = packagesToScan;
    }

}
