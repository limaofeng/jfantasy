package com.fantasy.mall.cart.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.annotate.JsonProperty;

import com.fantasy.framework.dao.mybatis.keygen.GUIDKeyGenerator;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.common.Base64Util;
import com.fantasy.framework.util.web.WebUtil;
import com.fantasy.framework.util.web.context.ActionContext;
import com.fantasy.mall.cart.bean.CartItem;

/**
 * 未登陆时，使用cookie购物车
 * 
 * @author 李茂峰
 * @since 2013-6-26 下午07:33:05
 * @version 1.0
 */
public class ShopCartCookie extends ShopCart {

	private static final long serialVersionUID = -1761315028414241176L;

	private static CartService cartService;

	private ShopCart shopCart;

	private synchronized static CartService getCartService() {
		if (cartService == null) {
			cartService = SpringContextUtil.getBeanByType(CartService.class);
		}
		return cartService;
	}

	public ShopCartCookie(HttpServletRequest request, HttpServletResponse response) {
		Cookie cookie = WebUtil.getCookie(request, CURRENT_SHOP_CART_KEY);
		String key = cookie == null ? GUIDKeyGenerator.getInstance().getGUID() : new String(Base64Util.decode(cookie.getValue().getBytes()));
		shopCart = getCartService().getShopCart(key, OwnerType.Cookie);
		if (cookie == null) {
			WebUtil.addCookie(response, CURRENT_SHOP_CART_KEY, new String(Base64Util.encode(key.getBytes())), 60 * 60 * 60 * 24);
		}
	}

	@Override
	public void clear() {
		getCartService().delete(shopCart);
		ActionContext.getContext().getSession().remove(CURRENT_SHOP_CART_KEY);
		WebUtil.removeCookie(ActionContext.getContext().getHttpRequest(), ActionContext.getContext().getHttpResponse(), CURRENT_SHOP_CART_KEY);
	}

	@Override
	protected void delete(CartItem item) {
		shopCart.delete(item);
	}

	@Override
	protected CartItem insert(CartItem item) {
		return shopCart.insert(item);
	}

	@Override
	protected CartItem update(CartItem item) {
		return shopCart.update(item);
	}

	@Override
	public CartItem addItem(String sn, Integer quantity) {
		return shopCart.addItem(sn, quantity);
	}

	@Override
	public int getTotalQuantity() {
		return shopCart.getTotalQuantity();
	}

	@Override
	public double getTotalPrice() {
		return shopCart.getTotalPrice();
	}

	@Override
	public CartItem[] removeItem(String... sns) {
		return shopCart.removeItem(sns);
	}

	@Override
	public CartItem updateItem(String sn, Integer quantity) {
		return shopCart.updateItem(sn, quantity);
	}

	@Override
	public Long getId() {
		return shopCart.getId();
	}

	@Override
	public void setId(Long id) {
		shopCart.setId(id);
	}

	@Override
	@JsonProperty("cartItems")
	public List<CartItem> getCartItems() {
		return shopCart.getCartItems();
	}

	@Override
	public void setCartItems(List<CartItem> cartItems) {
		shopCart.setCartItems(cartItems);
	}

	@Override
	public String getOwner() {
		return shopCart.getOwner();
	}

	@Override
	public void setOwner(String owner) {
		shopCart.setOwner(owner);
	}

	@Override
	public OwnerType getOwnerType() {
		return shopCart.getOwnerType();
	}

	@Override
	public void setOwnerType(OwnerType ownerType) {
		shopCart.setOwnerType(ownerType);
	}

	@Override
	public Date getCreateTime() {
		return shopCart.getCreateTime();
	}

	@Override
	public void setCreateTime(Date createTime) {
		shopCart.setCreateTime(createTime);
	}

	@Override
	public Date getModifyTime() {
		return shopCart.getModifyTime();
	}

	@Override
	public void setModifyTime(Date modifyTime) {
		shopCart.setModifyTime(modifyTime);
	}

	@Override
	public String getCreator() {
		return shopCart.getCreator();
	}

	@Override
	public void setCreator(String creator) {
		shopCart.setCreator(creator);
	}

	@Override
	public String getModifier() {
		return shopCart.getModifier();
	}

	@Override
	public void setModifier(String modifier) {
		shopCart.setModifier(modifier);
	}

}