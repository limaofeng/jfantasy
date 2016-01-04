package org.jfantasy.mall.cart.dao;

import org.springframework.stereotype.Repository;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.mall.cart.bean.CartItem;

@Repository
public class CartItemDao extends HibernateDao<CartItem, Long>{

}
