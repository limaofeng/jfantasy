package com.fantasy.payment.order;

import com.fantasy.payment.bean.Payment;

/**
 * 支付订单接口
 */
public interface OrderDetailsService {

    /**
     * 查询订单信息
     *
     * @param orderSn 编号
     * @return OrderDetails
     */
    OrderDetails loadOrderBySn(String orderSn);

    /**
     * 获取支付成功后的异步通知接口地址
     *
     * @return url
     */
    String getNotifyUrl(String paymentSn);

    /**
     * 获取支付成功后的回调处理URL
     *
     * @return url
     */
    String getReturnUrl(String paymentSn);

    /**
     * 支付失败
     *
     * @param payment 支付信息
     */
    void payFailure(Payment payment);

    /**
     * 支付成功
     *
     * @param payment 支付信息
     */
    void paySuccess(Payment payment);

    /**
     * 订单访问地址
     * 用于支付完成后，或者支付时的查看地址。该方法如果返回 空对象 或者空字符串 ，将显示默认的订单支付信息
     *
     * @param orderSn 订单编码
     * @return {String} 例如 : /order.do?sn=SN_000123
     */
    String getShowUrl(String orderSn);

    /**
     * 支付完成后，跳转的地址
     *
     * @param paymentSn 支付SN
     * @return {String}
     */
    String getShowPaymentUrl(String paymentSn);
}
