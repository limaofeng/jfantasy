package com.fantasy.common.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("订单明细项")
public interface OrderItem {

    @ApiModelProperty("编号")
    String getSn();

    @ApiModelProperty("名称")
    String getName();

    @ApiModelProperty("数量")
    Integer getQuantity();

}
