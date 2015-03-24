package com.fantasy.mall.cart.service.handler;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.fantasy.mall.cart.bean.Cart.OwnerType;
import com.fantasy.mall.cart.bean.CartItem;
import com.fantasy.mall.cart.service.CartService;
import com.fantasy.mall.cart.service.ShopCart;
import com.fantasy.mall.goods.bean.Product;
import com.fantasy.member.userdetails.MemberUser;

/**
 * 会员登录时,初始化购物车信息
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-6-26 下午11:39:04
 * @version 1.0
 */
public class InitializeShopCartLoginSuccessHandler implements AuthenticationSuccessHandler {

	private static final Log logger = LogFactory.getLog(InitializeShopCartLoginSuccessHandler.class);

	@Autowired
	private CartService cartService;

	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		MemberUser details = (MemberUser) authentication.getPrincipal();
		if (details != null) {
			if (!details.getData().containsKey(ShopCart.CURRENT_SHOP_CART_KEY)) {
				details.data(ShopCart.CURRENT_SHOP_CART_KEY, cartService.getShopCart(details.getUsername(), OwnerType.Member));
				ShopCart shopCart = (ShopCart) details.data(ShopCart.CURRENT_SHOP_CART_KEY);
				// 合并购物车
				if (ShopCart.hasCookieShopCart()) {
					ShopCart cookieShopCart = ShopCart.currentByCookie();
					for (CartItem cartItem : cookieShopCart.getCartItems()) {
						shopCart.addItem(cartItem.getProduct().getSn(), cartItem.getQuantity());
					}
					cookieShopCart.clear();
				}
				if (logger.isDebugEnabled()) {
					StringBuffer buffer = new StringBuffer();
					buffer.append("\r\n初始化购物车信息:");
					for (CartItem cartItem : shopCart.getCartItems()) {
						Product product = cartItem.getProduct();
						buffer.append("\r\n=>商品名称:" + product.getName() + "\t单价:" + cartItem.getPrice() + "\t购买数量:" + cartItem.getQuantity());
					}
					buffer.append("\r\n=>总计:" + shopCart.getTotalPrice());
					logger.debug(buffer.toString());
				}
			}
		}
	}

}
