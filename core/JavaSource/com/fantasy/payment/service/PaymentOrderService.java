package com.fantasy.payment.service;

import com.fantasy.payment.bean.Payment;
import com.fantasy.payment.error.PaymentException;

import java.math.BigDecimal;

/**
 * 支付订单接口
 */
public interface PaymentOrderService {

    /**
     * 支付检查方法,不能支付时，抛出 PaymentException 异常 。比如：作废的订单应该不能支付。
     *
     * @param sn 订单号
     */
    void payCheck(String sn) throws PaymentException;

    /**
     * 获取订单的支付金额(总)
     *
     * @param sn 订单号
     * @return {BigDecimal}
     */
    public BigDecimal totalAmount(String sn);

    /**
     * 获取订单应付金额
     *
     * @param sn 订单号
     * @return {BigDecimal}
     */
    public BigDecimal amountPayable(String sn);

    /**
     * 支付类型
     *
     * @param sn 订单号
     * @return {支付类型}
     */
    public String paymentType(String sn);

    /**
     * 准备支付
     *
     * @param payment 支付信息
     */
    public void ready(Payment payment);

    /**
     * 支付成功
     *
     * @param payment 支付信息
     */
    public void success(Payment payment);

    /**
     * 订单访问地址
     * 用于支付完成后，或者支付时的查看地址。该方法如果返回 空对象 或者空字符串 ，将显示默认的订单支付信息
     *
     * @param sn 订单编码
     * @return {String} 例如 : /order.do?sn=SN_000123
     */
    public String url(String sn);
}
