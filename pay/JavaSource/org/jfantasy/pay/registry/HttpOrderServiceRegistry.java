package org.jfantasy.pay.registry;

import org.jfantasy.pay.order.OrderServiceRegistry;
import org.jfantasy.pay.order.entity.enums.CallType;
import org.jfantasy.pay.service.OrderServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class HttpOrderServiceRegistry implements OrderServiceRegistry {

    @Autowired
    private OrderServerService orderServerService;

    @Override
    public void register(CallType callType, String type, String description, Properties props) {
        orderServerService.save(callType, type, description, props);
    }

    @Override
    public void register(CallType callType, String[] types, String description, Properties props) {
        for (String type : types) {
            this.register(callType, type, description, props);
        }
    }
}
