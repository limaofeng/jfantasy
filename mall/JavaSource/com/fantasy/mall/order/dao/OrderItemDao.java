package com.fantasy.mall.order.dao;

import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.mall.order.bean.OrderItem;
import org.springframework.stereotype.Repository;

@Repository
public class OrderItemDao extends HibernateDao<OrderItem,Long> {
}
