package com.fantasy.payment.service;

import java.math.BigDecimal;

/**
 * 订单支付接口类
 */
public interface OrderDetails {

    /**
     * 订单编号
     *
     * @return String
     */
    public String getSN();

    /**
     * 获取订单类型
     *
     * @return String
     */
    public String getType();

    /**
     * 订单摘要
     *
     * @return String
     */
    public String getSubject();

    /**
     * 订单总金额
     *
     * @return BigDecimal
     */
    public BigDecimal getTotalFee();

    /**
     * 订单应付金额
     *
     * @return BigDecimal
     */
    public BigDecimal getPayableFee();

    /**
     * 订单是否可以进行支付
     *
     * @return boolean
     */
    public boolean isPayment();

}
