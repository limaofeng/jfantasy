package com.fantasy.common.order;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单接口类
 */
public interface Order {

    /**
     * 订单编号
     *
     * @return String
     */
    String getSN();

    /**
     * 获取订单类型
     *
     * @return String
     */
    String getType();

    /**
     * 订单摘要
     *
     * @return String
     */
    String getSubject();

    /**
     * 订单总金额
     *
     * @return BigDecimal
     */
    BigDecimal getTotalFee();

    /**
     * 订单应付金额
     *
     * @return BigDecimal
     */
    BigDecimal getPayableFee();

    /**
     * 订单是否可以进行支付
     *
     * @return boolean
     */
    boolean isPayment();

    /**
     * 获取订单项
     *
     * @return List<OrderItem>
     */
    List<OrderItem> getOrderItems();

    ShipAddress getShipAddress();

}
