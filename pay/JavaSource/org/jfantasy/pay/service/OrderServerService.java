package org.jfantasy.pay.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.pay.bean.OrderServer;
import org.jfantasy.pay.bean.OrderServer.CallType;
import org.jfantasy.pay.dao.OrderServerDao;
import org.jfantasy.pay.order.OrderServiceFactory;
import org.jfantasy.rpc.client.NettyClientFactory;
import org.jfantasy.rpc.proxy.RpcProxyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServerService {

    private final static Log LOG = LogFactory.getLog(OrderServerService.class);
    /**
     * 创建 OrderService 的超时时间
     */
    private final long timeoutInMillis = 1000;

    @Autowired
    private OrderServerDao orderServerDao;
    @Autowired
    private OrderServiceFactory orderServiceFactory;

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public Pager<OrderServer> findPager(Pager<OrderServer> pager, List<PropertyFilter> filters) {
        return this.orderServerDao.findPager(pager, filters);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void save(CallType callType, String type, String domain, String title, String description) {
        OrderServer orderServer = new OrderServer();
        orderServer.setCallType(callType);
        orderServer.setType(type);
        orderServer.set(OrderServer.PROPS_DOMAIN, domain);
        orderServer.setName(title);
        orderServer.setDescription(description);
        orderServer.setEnabled(true);//TODO 直接启用,后续改为人为修改
        this.orderServerDao.save(orderServer);
    }

    public List<OrderServer> find(Criterion... criterions) {
        return this.orderServerDao.find(criterions);
    }

    public void register(OrderServer entity) {
        String[] ars = entity.get(OrderServer.PROPS_DOMAIN).split(":");
        RpcProxyFactory rpcProxyFactory = new RpcProxyFactory(new NettyClientFactory(ars[0], Integer.valueOf(ars[1])));
        org.jfantasy.pay.order.OrderService orderService = rpcProxyFactory.proxyBean(org.jfantasy.pay.order.OrderService.class, timeoutInMillis);
        orderServiceFactory.register(entity.getType(), orderService);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void register() {
        for (OrderServer entity : this.find(Restrictions.eq("enabled", true))) {
            try {
                register(entity);
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }

    public void unregister(OrderServer entity) {
        orderServiceFactory.unregister(entity.getType());
    }

}
