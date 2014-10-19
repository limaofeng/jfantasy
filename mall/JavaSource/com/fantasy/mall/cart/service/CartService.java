package com.fantasy.mall.cart.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.mall.cart.bean.Cart;
import com.fantasy.mall.cart.bean.Cart.OwnerType;
import com.fantasy.mall.cart.bean.CartItem;
import com.fantasy.mall.cart.dao.CartDao;
import com.fantasy.mall.cart.dao.CartItemDao;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CartService {

	@Resource
	private CartDao cartDao;
	@Resource
	private CartItemDao cartItemDao;

	public void save(Cart cart) {
		this.cartDao.save(cart);
	}

	public void save(CartItem item) {
		this.cartItemDao.save(item);
	}
	
	public Pager<CartItem> findPager(Pager<CartItem> pager, List<PropertyFilter> filters) {
		return cartItemDao.findPager(pager, filters);
	}

	public ShopCart getShopCart(String owner, OwnerType ownerType) {
		Cart cart = this.cartDao.findUnique(Restrictions.eq("owner", owner), Restrictions.eq("ownerType", ownerType));
		if (cart == null) {
			cart = new Cart();
			cart.setOwner(owner);
			cart.setOwnerType(ownerType);
			cart.setCartItems(new ArrayList<CartItem>());
			cartDao.save(cart);
		}
		return new ShopCartDataBase(cart);
	}

	public void delete(CartItem item) {
		this.cartItemDao.delete(item);
	}

	public void delete(Cart item) {
		this.cartDao.delete(item.getId());
	}

}
