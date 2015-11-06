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
     * 支付请求参数
     *
     * @param parameters 请求参数
     * @return Map
     */
    Map<String, String> getParameterMap(Parameters parameters);

    /**
     * 支付验证方法
     *
     * @param parameters 请求参数
     * @return boolean
     */
    boolean verifySign(Map<String, String> parameters);

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
     * @param sParaTemp 请求参数数组
     * @return 提交表单HTML文本
     */
    String buildRequest(Map<String, String> sParaTemp);

    /**
     * 解析支付结果
     *
     * @param parameters 支付结果
     * @return PayResult
     */
    PayResult parsePayResult(Map<String, String> parameters);


    /**
     * 支付产品标示
     *
     * @return String
     */
    String getId();

    /**
     * 支付产品名称
     *
     * @return String
     */
    String getName();

    /**
     * 收款方账号
     *
     * @return String
     */
    String getShroffAccountName();

    /**
     * 商户ID参数名称
     *
     * @return String
     */
    String getBargainorIdName();

    /**
     * 密钥参数名称
     *
     * @return String
     */
    String getBargainorKeyName();

    /**
     * 支付产品描述
     *
     * @return String
     */
    String getDescription();

    /**
     * 支付产品LOGO路径
     *
     * @return String
     */
    String getLogoPath();

    /**
     * 支持货币类型
     *
     * @return CurrencyType[]
     */
    CurrencyType[] getCurrencyTypes();
}
