package org.jfantasy.pay.order;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfantasy.pay.order.entity.enums.PaymentStatus;
import org.jfantasy.pay.order.entity.enums.RefundStatus;
import org.jfantasy.rpc.annotation.ServiceExporter;
import org.jfantasy.rpc.client.NettyClientFactory;
import org.jfantasy.rpc.proxy.RpcProxyFactory;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceExporter(targetInterface = OrderServiceRegistry.class)
public class OrderServiceRegistryImpl implements OrderServiceRegistry {

    private final static Log LOG = LogFactory.getLog(OrderServiceRegistryImpl.class);

    /**
     * OrderServiceFactory
     */
    @Autowired
    private OrderServiceFactory orderServiceFactory;
    /**
     * 创建 OrderService 的超时时间
     */
    private long timeoutInMillis = 1000;

    @Override
    public void register(String name, String description, String host, int port) {
        //TODO 将 OrderServiceRegistry 写入到数据库中
        RpcProxyFactory rpcProxyFactory = new RpcProxyFactory(new NettyClientFactory(host, port));
        OrderService orderService = rpcProxyFactory.proxyBean(OrderService.class, timeoutInMillis);
        orderServiceFactory.register(orderService.types(), orderService);
        orderService.on("test:t000001", PaymentStatus.close, "");
        orderService.on("test:t000001", RefundStatus.failure, "");
        LOG.debug(" OrderService " + name + " register success !");
    }

}
