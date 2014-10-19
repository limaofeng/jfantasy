package com.fantasy.mall.cart.dao;

import org.springframework.stereotype.Repository;

import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.mall.cart.bean.CartItem;

@Repository
public class CartItemDao extends HibernateDao<CartItem, Long>{

}
