package org.jfantasy.pay.boot;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Restrictions;
import org.jfantasy.pay.bean.OrderServer;
import org.jfantasy.pay.order.OrderServiceFactory;
import org.jfantasy.pay.service.OrderServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceCommandLineRunner implements CommandLineRunner {

    private static final Log LOG = LogFactory.getLog(OrderServiceCommandLineRunner.class);

    @Autowired
    private OrderServiceFactory orderServiceFactory;
    @Autowired
    private OrderServerService orderServerService;

    @Override
    public void run(String... args) throws Exception {
        for (OrderServer entity : orderServerService.find(Restrictions.eq("enabled", true))) {
            orderServiceFactory.register(entity.getType(), orderServiceFactory.getBuilder(entity.getCallType()).build(entity.getProperties()));
        }
    }

}
