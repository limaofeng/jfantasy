package com.fantasy.mall.shop.dao;

import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.mall.shop.bean.Shop;
import org.springframework.stereotype.Repository;

@Repository
public class ShopDao extends HibernateDao<Shop,Long> {
}
