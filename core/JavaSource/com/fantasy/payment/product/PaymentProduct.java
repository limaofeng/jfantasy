package com.fantasy.payment.product;

import com.fantasy.payment.bean.PaymentConfig;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 支付产品接口
 */
public interface PaymentProduct {

    String getPaymentUrl();

    String getName();

    Map<String, String> getParameterMap(PaymentConfig paymentConfig, String sn, BigDecimal amount, Map<String, String> parameters);

    boolean verifySign(PaymentConfig paymentConfig, Map<String, String> parameters);

    boolean isPaySuccess(Map<String, String> parameters);

    //BigDecimal getPaymentAmount(Map<String, String> parameters);

    String getPayreturnMessage(String sn);
}
