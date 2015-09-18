package com.fantasy.payment.product;

import java.util.Map;


public class WxpayProduct extends AbstractPaymentProduct {

    @Override
    public String getPaymentUrl() {
        return "https://api.mch.weixin.qq.com/pay/unifiedorder";
    }

    @Override
    public Map<String, String> getParameterMap(Map<String, String> parameters) {
        return null;
    }

    @Override
    public boolean verifySign(Map<String, String> parameters) {
        return false;
    }

    @Override
    public PayResult parsePayResult(Map<String, String> parameters) {
        return null;
    }

}
