package org.jfantasy.pay.order;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfantasy.pay.bean.OrderServer;
import org.jfantasy.pay.service.OrderServerService;
import org.jfantasy.rpc.annotation.ServiceExporter;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceExporter(targetInterface = OrderServiceRegistry.class)
public class OrderServiceRegistryImpl implements OrderServiceRegistry {

    private final static Log LOG = LogFactory.getLog(OrderServiceRegistryImpl.class);

    @Autowired
    private OrderServerService orderServerService;

    @Override
    public void register(String type, String title, String description, String host, int port) {
        orderServerService.save(OrderServer.CallType.rpc, type, host + ":" + port, title, description);
    }

    @Override
    public void register(String[] types, String title, String description, String host, int port) {
        for (String type : types) {
            this.register(type, title, description, host, port);
        }
    }

}
