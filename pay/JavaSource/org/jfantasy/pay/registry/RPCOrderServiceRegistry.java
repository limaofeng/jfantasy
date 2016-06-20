package org.jfantasy.pay.registry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfantasy.pay.order.OrderServiceRegistry;
import org.jfantasy.pay.order.entity.enums.CallType;
import org.jfantasy.pay.service.OrderServerService;
import org.jfantasy.rpc.annotation.ServiceExporter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Properties;

@ServiceExporter(value = "rpcOrderServiceRegistry", targetInterface = OrderServiceRegistry.class)
public class RpcOrderServiceRegistry implements OrderServiceRegistry {

    private final static Log LOG = LogFactory.getLog(RpcOrderServiceRegistry.class);

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
