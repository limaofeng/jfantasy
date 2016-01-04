package org.jfantasy.mall.cart.service;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.mall.cart.bean.Cart;
import org.jfantasy.mall.cart.bean.Cart.OwnerType;
import org.jfantasy.mall.cart.bean.CartItem;
import org.jfantasy.mall.cart.dao.CartDao;
import org.jfantasy.mall.cart.dao.CartItemDao;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CartService {

	@Autowired
	private CartDao cartDao;
	@Autowired
	private CartItemDao cartItemDao;

	public Cart save(Cart cart) {
		return this.cartDao.save(cart);
	}

	public CartItem save(CartItem item) {
		return this.cartItemDao.save(item);
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
			cart = cartDao.save(cart);
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
