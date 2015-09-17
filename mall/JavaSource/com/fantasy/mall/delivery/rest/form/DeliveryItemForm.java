package com.fantasy.mall.delivery.rest.form;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel("物流项")
public class DeliveryItemForm {

    @ApiModelProperty("货品编号")
    private String sn;
    @ApiModelProperty("物流数量")
    private Integer quantity;

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

}
