package com.fantasy.payment.service;

import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.payment.bean.Payment;
import com.fantasy.payment.error.PaymentException;

import java.math.BigDecimal;

public class TestPaymentOrderService implements PaymentOrderService {

    @Override
    public void payCheck(String sn) throws PaymentException {
        PaymentService paymentService = SpringContextUtil.getBeanByType(PaymentService.class);
    }

    @Override
    public BigDecimal totalAmount(String sn) {
        return BigDecimal.valueOf(Double.valueOf("0.01"));
    }

    @Override
    public BigDecimal amountPayable(String sn) {
        return BigDecimal.valueOf(Double.valueOf("0.01"));
    }

    @Override
    public String paymentType(String sn) {
        return "test";
    }

    @Override
    public void ready(Payment payment) {

    }

    @Override
    public void success(Payment payment) {

    }

    @Override
    public String url(String sn) {
        return null;
    }

}
