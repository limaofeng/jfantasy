package org.jfantasy.mall.delivery.dao;

import org.springframework.stereotype.Repository;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.mall.delivery.bean.DeliveryType;

@Repository("mall.dao.DeliveryTypeDao")
public class DeliveryTypeDao extends HibernateDao<DeliveryType, Long> {

}
