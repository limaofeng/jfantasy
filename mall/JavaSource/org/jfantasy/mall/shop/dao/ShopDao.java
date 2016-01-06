package org.jfantasy.mall.shop.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.mall.shop.bean.Shop;
import org.springframework.stereotype.Repository;

@Repository
public class ShopDao extends HibernateDao<Shop,Long> {
}
