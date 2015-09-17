package com.fantasy.mall.delivery.rest.form;

import com.fantasy.mall.delivery.bean.DeliveryItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

@ApiModel("发货表单")
public class ShippingForm {
    /**
     * 配送方式
     */
    @ApiModelProperty("配送方式")
    private Long deliveryTypeId;
    /**
     * 订单编号
     */
    @ApiModelProperty("订单编号")
    private String orderSn;
    /**
     * 订单类型
     */
    @ApiModelProperty("订单类型")
    private String orderType;
    /**
     * 物流项
     */
    @ApiModelProperty("物流项")
    private List<DeliveryItemForm> items;

    public Long getDeliveryTypeId() {
        return deliveryTypeId;
    }

    public void setDeliveryTypeId(Long deliveryTypeId) {
        this.deliveryTypeId = deliveryTypeId;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public List<DeliveryItemForm> getItems() {
        return items;
    }

    public void setItems(List<DeliveryItemForm> items) {
        this.items = items;
    }

    @JsonIgnore
    public List<DeliveryItem> getDeliveryItems() {
        List<DeliveryItem> deliveryItems = new ArrayList<DeliveryItem>();
        for (DeliveryItemForm itemForm : this.getItems()) {
            DeliveryItem deliveryItem = new DeliveryItem();
            deliveryItem.setSn(itemForm.getSn());
            deliveryItem.setQuantity(itemForm.getQuantity());
            deliveryItems.add(deliveryItem);
        }
        return deliveryItems;
    }

}
