package com.fantasy.payment.product;

import java.util.Map;

/**
 * 支付产品接口
 */
public interface PaymentProduct {

    /**
     * 支付地址
     *
     * @return String
     */
    String getPaymentUrl();

    /**
     * 支付产品名称
     *
     * @return String
     */
    String getName();

    /**
     * 支付请求参数
     *
     * @param parameters 请求参数
     * @return Map
     */
    Map<String, String> getParameterMap(Map<String, String> parameters);

    /**
     * 支付验证方法
     *
     * @param parameters 请求参数
     * @return boolean
     */
    boolean verifySign(Map<String, String> parameters);

    /**
     * 判断支付是否成功
     *
     * @param parameters 请求参数
     * @return boolean
     */
    boolean isPaySuccess(Map<String, String> parameters);

    /**
     * 根据支付编号获取支付返回信息
     *
     * @return String
     */
    String getPayreturnMessage(String paymentSn);

    /**
     * 获取支付通知信息
     *
     * @return String
     */
    String getPaynotifyMessage(String paymentSn);

    /**
     * 建立请求，以表单HTML形式构造（默认）
     *
     * @param sParaTemp     请求参数数组
     * @return 提交表单HTML文本
     */
    public String buildRequest(Map<String, String> sParaTemp);
}
