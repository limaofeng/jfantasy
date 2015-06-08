package com.fantasy.mall.cart.web;

import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.mall.cart.service.ShopCart;

public class CartItemAction extends ActionSupport {

    private static final long serialVersionUID = -7331376253783324490L;


    public String execute() {
        this.attrs.put("cart",ShopCart.current());
        return SUCCESS;
    }

    /**
     * 添加购物车
     * <p/>
     * 暂时只提供简单的添加功能，类似颜色、款式、服务等选择后续添加
     *
     * @param sn 商品编号
     * @param q  购买数量
     * @return {String}
     */
    public String add(String sn, Integer q) {
        ShopCart.current().addItem(sn, q);
        this.attrs.put(ROOT, ShopCart.current());
        return JSONDATA;
    }

    /**
     * 清空购物车
     *
     * @return {String}
     */
    public String clear() {
        ShopCart.current().clear();
        return JSONDATA;
    }

    /**
     * 移除商品
     *
     * @param sns 商品货号数组
     * @return {String}
     */
    public String remove(String... sns) {
        this.attrs.put(ROOT, ShopCart.current().removeItem(sns));
        return JSONDATA;
    }

    /**
     * 编辑商品数量
     *
     * @param sn 商品货号
     * @param q  调整数量
     * @return {String}
     */
    public String edit(String sn, Integer q) {
        this.attrs.put(ROOT, ShopCart.current().updateItem(sn, q));
        return JSONDATA;
    }

}
