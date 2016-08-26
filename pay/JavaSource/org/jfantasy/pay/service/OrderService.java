package org.jfantasy.pay.service;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.spring.mvc.error.RestException;
import org.jfantasy.pay.bean.Order;
import org.jfantasy.pay.dao.OrderDao;
import org.jfantasy.pay.order.OrderServiceFactory;
import org.jfantasy.pay.order.entity.OrderDetails;
import org.jfantasy.pay.order.entity.OrderKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 订单明细记录
 */
@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderServiceFactory orderServiceFactory;

    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    public Order get(OrderKey key) {
        return this.orderDao.get(key);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public Pager<Order> findPager(Pager<Order> pager, List<PropertyFilter> filters) {
        return this.orderDao.findPager(pager, filters);
    }

    public Order save(OrderDetails details) {
        Order order = new Order();
        order.setSn(details.getSn());
        order.setType(details.getType());
        order.setSubject(details.getSubject());
        order.setBody(details.getBody());
        order.setPayableFee(details.getPayableFee());
        order.setTotalFee(details.getTotalFee());
        order.setStatus(Order.PaymentStatus.unpaid);
        order.setOrderItems(details.getOrderItems());
        order.setMemberId(details.getMemberId());
        return this.orderDao.save(order);
    }

    /**
     * 查询 key 对应的订单信息
     *
     * @param key 订单Key
     * @return Order 中包含 OrderDetails 的信息
     */
    @Transactional
    public Order getOrder(OrderKey key) {
        Order order = this.orderDao.get(key);
        OrderDetails orderDetails;
        if (order == null) {
            if (!orderServiceFactory.containsType(key.getType())) {
                throw new RestException("orderType[" + key.getType() + "] 对应的 PaymentOrderService 未配置！");
            }
            //获取订单信息
            orderDetails = orderServiceFactory.getOrderService(key.getType()).loadOrder(key);
            if (orderDetails == null) {
                throw new RestException("order = [" + key + "] 不存在,请核对后,再继续操作!");
            }
            order = this.save(orderDetails);
        } else {
            orderDetails = orderServiceFactory.getOrderService(key.getType()).loadOrder(key);
        }
        order.setDetails(orderDetails);
        return order;
    }

    public void update(Order order) {
        this.orderDao.update(order);
    }

}
