package com.fantasy.mall.cart.service;

import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.web.WebUtil;
import com.fantasy.framework.util.web.context.ActionContext;
import com.fantasy.mall.cart.bean.Cart;
import com.fantasy.mall.cart.bean.CartItem;
import com.fantasy.mall.goods.service.ProductService;
import com.fantasy.member.userdetails.MemberUser;
import com.fantasy.security.SpringSecurityUtils;
import com.fantasy.security.userdetails.SimpleUser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 购物车 业务实体
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-6-26 下午07:32:34
 */
public abstract class ShopCart extends Cart {

    private static final long serialVersionUID = 3927319282450699367L;

    public static final String CURRENT_SHOP_CART_KEY = "shopcart";

    /**
     * 获取当前用户的购物车
     *
     * @return {ShopCart}
     */
    public static ShopCart current() {
        if (SpringSecurityUtils.getCurrentUser() != null && SpringSecurityUtils.getCurrentUser() instanceof MemberUser) {
            return (ShopCart) SpringSecurityUtils.getCurrentUser(SimpleUser.class).data(CURRENT_SHOP_CART_KEY);
        } else {
            return currentByCookie();
        }
    }

    /**
     * 是否存在cookie购物车
     *
     * @return {boolean}
     */
    public static boolean hasCookieShopCart() {
        Map<String, Object> session = ActionContext.getContext().getSession();
        Object object = session.get(CURRENT_SHOP_CART_KEY);
        return object != null || WebUtil.getCookie(ActionContext.getContext().getHttpRequest(), CURRENT_SHOP_CART_KEY) != null;
    }

    /**
     * 获取当前用户的cookie购物车
     *
     * @return {ShopCart}
     */
    public static ShopCart currentByCookie() {
        Map<String, Object> session = ActionContext.getContext().getSession();
        Object object = session.get(CURRENT_SHOP_CART_KEY);
        if (object instanceof ShopCart) {
            return (ShopCart) object;
        } else {
            ShopCart shopCart = new ShopCartCookie(ActionContext.getContext().getHttpRequest(), ActionContext.getContext().getHttpResponse());
            session.put(CURRENT_SHOP_CART_KEY, shopCart);
            return shopCart;
        }
    }

    /**
     * 向购物车添加商品
     *
     * @param sn       货品编号
     * @param quantity 数量
     * @return {CartItem}
     */
    public CartItem addItem(String sn, Integer quantity) {
        int index = ObjectUtil.indexOf(this.getCartItems(), "product.sn", sn);
        if (quantity < 1) {
            throw new CartException("购物车不能添加商品时,购买数量不能为负数或者零!");
        }
        if (index == -1) {
            CartItem item = new CartItem();
            item.setCart(this);
            item.setProduct(ProductService.getProductBySn(sn));
            item.setQuantity(quantity);
            insert(item);
            this.getCartItems().add(copyCartItem(new CartItem(), item));
            return item;
        } else {
            CartItem item = this.getCartItems().get(index);
            if ((item.getQuantity() + quantity) <= 0) {
                this.removeItem(item.getProduct().getSn());
            } else {
                item.setQuantity(item.getQuantity() + quantity);
                update(item);
            }
            return item;
        }
    }

    /**
     * 获取总数量
     */
    public int getTotalQuantity() {
        int totalQuantity = 0;
        for (CartItem item : this.getCartItems()) {
            totalQuantity += item.getQuantity();
        }
        return totalQuantity;
    }

    /**
     * 获取总价格
     *
     * @return {double}
     */
    public double getTotalPrice() {
        BigDecimal totalPrice = new BigDecimal(0);
        for (CartItem item : this.getCartItems()) {
            totalPrice = totalPrice.add(item.getSubtotalPrice());
        }
        return totalPrice.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 清空购物车
     */
    public void clear() {
        try {
            for (CartItem item : this.getCartItems()) {
                delete(item);
            }
        } finally {
            this.getCartItems().clear();
        }
    }

    public CartItem removeItem(String sn, Integer quantity) {
        int index = ObjectUtil.indexOf(this.getCartItems(), "product.sn", sn);
        if (index == -1) {
            return null;
        }
        CartItem item = this.getCartItemBySn(sn);
        if ((item.getQuantity() - quantity) <= 0) {
            this.removeItem(item.getProduct().getSn());
        } else {
            item.setQuantity(item.getQuantity() - quantity);
            update(item);
        }
        return item;
    }

    /**
     * 从购物车中删除商品
     *
     * @param sns 商品货号数组
     * @return {CartItem[]}
     */
    public CartItem[] removeItem(String... sns) {
        List<CartItem> cartItems = new ArrayList<CartItem>();
        for (String sn : sns) {
            int index = ObjectUtil.indexOf(this.getCartItems(), "product.sn", sn);
            if (index != -1) {
                CartItem item = this.getCartItems().get(index);
                delete(item);
                cartItems.add(ObjectUtil.remove(this.getCartItems(), "product.sn", sn));
            }
        }
        return cartItems.toArray(new CartItem[cartItems.size()]);
    }

    /**
     * 更新购物车信息
     *
     * @param sn 商品货号
     * @param quantity 更新数量
     * @return {CartItem}
     */
    public CartItem updateItem(String sn, Integer quantity) {
        int index = ObjectUtil.indexOf(this.getCartItems(), "product.sn", sn);
        if (index != -1) {
            CartItem item = this.getCartItemBySn(sn);
            if (quantity == null || quantity <= 0) {
                return item;
            }
            if (quantity >= item.getProduct().getSurplusStore()) {
                quantity = item.getProduct().getSurplusStore();
            }
            item.setQuantity(quantity);
            update(item);
            return item;
        }
        throw new CartException("购物车中没有该商品的信息");
    }

    /**
     * 获取购物车中的信息
     *
     * @param sn 商品货号
     * @return {CartItem} 返回购物项信息
     */
    public CartItem getCartItemBySn(String sn) {
        CartItem item = ObjectUtil.find(this.getCartItems(), "product.sn", sn);
        item.setProduct(ProductService.getProductBySn(item.getProduct().getSn()));
        return item;
    }

    // TODO 每次访问购物车时，去查询最新的product信息.(是否会造成不必要的性能消耗)
    @Override
    public List<CartItem> getCartItems() {
        if (!WebUtil.isAjax()) {
            for (CartItem cartItem : super.getCartItems()) {
                cartItem.setProduct(ProductService.getProductBySn(cartItem.getProduct().getSn()));
            }
        }
        return super.getCartItems();
    }

    protected abstract CartItem insert(CartItem item);

    protected abstract CartItem update(CartItem item);

    protected abstract void delete(CartItem item);

    protected CartItem copyCartItem(CartItem dest, CartItem orig) {
//        dest = ObjectUtil.copy(dest, orig, "cart", "product");
        //TODO copy
        dest.setProduct(ProductService.copyProduct(orig.getProduct()));
        return dest;
    }

    protected Cart copyCart(Cart dest, Cart orig) {
//       TODO  ObjectUtil.copy(dest, orig, "cartItems");
        dest.setId(orig.getId());
        dest.setOwner(orig.getOwner());
        dest.setOwnerType(orig.getOwnerType());
        dest.setCreator(orig.getCreator());
        dest.setCreateTime(orig.getCreateTime());
        dest.setModifier(orig.getModifier());
        dest.setModifyTime(orig.getModifyTime());
        List<CartItem> cartItems = new ArrayList<CartItem>();
        for (CartItem cartItem : orig.getCartItems()) {
            cartItems.add(copyCartItem(new CartItem(), cartItem));
        }
        dest.setCartItems(cartItems);
        return dest;
    }

}
