package com.fantasy.payment.product;

import com.fantasy.payment.bean.PaymentConfig;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 支付产品接口
 */
public interface PaymentProduct {

    String getPaymentUrl();

    String getName();

    Map<String, String> getParameterMap(PaymentConfig paymentConfig, String sn, BigDecimal amount, HttpServletRequest request);

    boolean verifySign(PaymentConfig paymentConfig, HttpServletRequest request);

    boolean isPaySuccess(HttpServletRequest request);

    BigDecimal getPaymentAmount(HttpServletRequest request);

    String getPayreturnMessage(String sn);
}
