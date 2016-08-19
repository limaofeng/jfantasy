package org.jfantasy.pay.order;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfantasy.rpc.config.NettyLocalSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceRegistryRunner implements CommandLineRunner {

    public static final Log LOG = LogFactory.getLog(OrderServiceRegistryRunner.class);

    @Autowired
    public NettyLocalSettings nettyLocalSettings;

    @Autowired
    private OrderServiceRegistry orderServiceRegistry;

    @Autowired
    private OrderService orderService;

    @Override
    public void run(String... args) throws Exception {
        try {
            orderServiceRegistry.register(orderService.types(), nettyLocalSettings.getHost(), nettyLocalSettings.getPort());
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

}
