package com.fantasy.payment.service;

import com.fantasy.payment.bean.Payment;

/**
 * 支付订单接口
 */
public interface PaymentOrderDetailsService {

    /**
     * 查询订单信息
     *
     * @param sn 编号
     * @return OrderDetails
     */
    OrderDetails loadOrderBySn(String sn);

//    /**
//     * 支付检查方法,不能支付时，抛出 PaymentException 异常 。比如：作废的订单应该不能支付。
//     *
//     * @param sn 订单号
//     */
//    void payCheck(String sn) throws PaymentException;

    /**
     * 支付失败
     *
     * @param payment 支付信息
     */
    public void payFailure(Payment payment);

    /**
     * 支付成功
     *
     * @param payment 支付信息
     */
    public void paySuccess(Payment payment);

    /**
     * 订单访问地址
     * 用于支付完成后，或者支付时的查看地址。该方法如果返回 空对象 或者空字符串 ，将显示默认的订单支付信息
     *
     * @param sn 订单编码
     * @return {String} 例如 : /order.do?sn=SN_000123
     */
    public String url(String sn);
}
