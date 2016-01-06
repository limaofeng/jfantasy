package org.jfantasy.mall.cart.service;

import org.jfantasy.framework.spring.SpringContextUtil;
import org.jfantasy.mall.cart.bean.Cart;
import org.jfantasy.mall.cart.bean.CartItem;

/**
 * 数据库的购物车
 * 
 * @author 李茂峰
 * @since 2013-6-26 下午07:32:51
 * @version 1.0
 */
public class ShopCartDataBase extends ShopCart {

	private static final long serialVersionUID = -3417224993235792253L;

	private static CartService cartService;

	private synchronized static CartService getCartService() {
		if (cartService == null) {
			cartService = SpringContextUtil.getBeanByType(CartService.class);
		}
		return cartService;
	}

	public ShopCartDataBase(Cart cart) {
		super.copyCart(this,cart);
	}

	@Override
	protected void delete(CartItem item) {
		getCartService().delete(item);
	}

	@Override
	protected CartItem insert(CartItem item) {
		getCartService().save(item);
		return item;
	}

	@Override
	protected CartItem update(CartItem item) {
		getCartService().save(item);
		return item;
	}

}
