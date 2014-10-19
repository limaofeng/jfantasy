package com.fantasy.mall.delivery.dao;

import org.springframework.stereotype.Repository;

import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.mall.delivery.bean.DeliveryType;

@Repository("mall.dao.DeliveryTypeDao")
public class DeliveryTypeDao extends HibernateDao<DeliveryType, Long> {

}
