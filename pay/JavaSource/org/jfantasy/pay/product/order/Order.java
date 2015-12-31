package org.jfantasy.pay.product.order;

import com.fantasy.common.order.ShipAddress;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单接口类
 */
@ApiModel("订单接口")
public interface Order {

    /**
     * 订单编号
     *
     * @return String
     */
    @ApiModelProperty("编号")
    String getSN();

    /**
     * 获取订单类型
     *
     * @return String
     */
    @ApiModelProperty("订单类型")
    String getType();

    /**
     * 订单摘要
     *
     * @return String
     */
    @ApiModelProperty("订单摘要")
    String getSubject();

    /**
     * 订单总金额
     *
     * @return BigDecimal
     */
    @ApiModelProperty("订单总金额")
    BigDecimal getTotalFee();

    /**
     * 订单应付金额
     *
     * @return BigDecimal
     */
    @ApiModelProperty("订单应付金额")
    BigDecimal getPayableFee();

    /**
     * 订单是否可以进行支付
     *
     * @return boolean
     */
    @ApiModelProperty("订单是否可以进行支付")
    boolean isPayment();

    /**
     * 获取订单项
     *
     * @return List<OrderItem>
     */
    @ApiModelProperty("订单项")
    List<OrderItem> getOrderItems();

    @ApiModelProperty("订单的配送地址")
    ShipAddress getShipAddress();

}
