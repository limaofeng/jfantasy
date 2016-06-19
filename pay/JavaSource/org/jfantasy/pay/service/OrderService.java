package org.jfantasy.pay.service;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.pay.bean.Order;
import org.jfantasy.pay.dao.OrderDao;
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
        return this.orderDao.save(order);
    }

}
