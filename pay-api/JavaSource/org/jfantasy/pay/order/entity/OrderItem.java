package org.jfantasy.pay.order.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("订单明细项")
public class OrderItem {
    @ApiModelProperty("编号")
    private String sn;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("数量")
    private int quantity;

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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
