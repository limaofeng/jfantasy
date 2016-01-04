package org.jfantasy.mall.cart.dao;

import org.springframework.stereotype.Repository;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.mall.cart.bean.Cart;

@Repository
public class CartDao extends HibernateDao<Cart, Long> {

}
