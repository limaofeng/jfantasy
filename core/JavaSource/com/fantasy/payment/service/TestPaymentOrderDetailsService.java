package com.fantasy.payment.service;

import com.fantasy.payment.bean.Payment;

import java.math.BigDecimal;

public class TestPaymentOrderDetailsService implements PaymentOrderDetailsService {

    @Override
    public OrderDetails loadOrderBySn(final String sn) {
        return new OrderDetails() {

            @Override
            public String getSN() {
                return sn;
            }

            @Override
            public String getType() {
                return "TEST";
            }

            @Override
            public String getSubject() {
                return "测试订单";
            }

            @Override
            public BigDecimal getTotalFee() {
                return BigDecimal.valueOf(0.01);
            }

            @Override
            public BigDecimal getPayableFee() {
                return BigDecimal.valueOf(0.01);
            }

            @Override
            public boolean isPayment() {
                return true;
            }

        };
    }

    @Override
    public void payFailure(Payment payment) {

    }

    @Override
    public void paySuccess(Payment payment) {

    }

    @Override
    public String url(String sn) {
        return null;
    }
}
