package com.fantasy.mall.order.ws.dto;


import com.fantasy.mall.goods.ws.dto.GoodsDTO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class OrderItemDTO  implements Serializable {


    private Long id;

    private String sn;// 商品货号

    private String name;// 商品名称
    /**
     * 商品价格
     */

    private BigDecimal productPrice;
    /**
     * 商品数量
     */
    private Integer productQuantity;

   private Integer deliveryQuantity;// 发货数量

    private OrderDTO order;// 订单

    private GoodsDTO goods;// 商品

   // private String attr; // 商品属性

    /**
     * 优惠套餐名称
     */

    private String rentCombinationName;
    /**
     * 优惠套餐时长
     */

    private Integer rentCombinationTime;
    /**
     * 优惠套餐价格
     */

    private BigDecimal rentCombinationPrice;

    /**
     * 预定开始时间
     */

    private Date bookTime;

    /**
     * 预定时长
     */

    private Integer time;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Integer productQuantity) {
        this.productQuantity = productQuantity;
    }

    public OrderDTO getOrder() {
        return order;
    }

    public void setOrder(OrderDTO order) {
        this.order = order;
    }

    public GoodsDTO getGoods() {
        return goods;
    }

    public void setGoods(GoodsDTO goods) {
        this.goods = goods;
    }

    public String getRentCombinationName() {
        return rentCombinationName;
    }

    public void setRentCombinationName(String rentCombinationName) {
        this.rentCombinationName = rentCombinationName;
    }

    public Integer getRentCombinationTime() {
        return rentCombinationTime;
    }

    public void setRentCombinationTime(Integer rentCombinationTime) {
        this.rentCombinationTime = rentCombinationTime;
    }

    public BigDecimal getRentCombinationPrice() {
        return rentCombinationPrice;
    }

    public void setRentCombinationPrice(BigDecimal rentCombinationPrice) {
        this.rentCombinationPrice = rentCombinationPrice;
    }

    public Date getBookTime() {
        return bookTime;
    }

    public void setBookTime(Date bookTime) {
        this.bookTime = bookTime;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getDeliveryQuantity() {
        return deliveryQuantity;
    }

    public void setDeliveryQuantity(Integer deliveryQuantity) {
        this.deliveryQuantity = deliveryQuantity;
    }
}
