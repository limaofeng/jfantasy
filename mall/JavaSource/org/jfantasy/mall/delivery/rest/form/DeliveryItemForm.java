package org.jfantasy.mall.delivery.rest.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

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
