package com.fantasy.mall.cart.dao;

import org.springframework.stereotype.Repository;

import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.mall.cart.bean.Cart;

@Repository
public class CartDao extends HibernateDao<Cart, Long> {

}
