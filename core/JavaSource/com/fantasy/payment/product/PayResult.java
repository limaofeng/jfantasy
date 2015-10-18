package com.fantasy.payment.product;

import java.math.BigDecimal;

public class PayResult {

    public enum PayStatus {
        /**
         * 失败
         */
        failure,
        /**
         * 成功
         */
        success
    }

    /**
     * 交易流水号
     */
    private String tradeNo;
    /**
     * 系统支付流水
     */
    private String paymentSN;
    /**
     * 交易状态
     */
    private PayStatus status;
    /**
     * 交易金额
     */
    private BigDecimal totalFee;

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public PayStatus getStatus() {
        return status;
    }

    public void setStatus(PayStatus status) {
        this.status = status;
    }

    public BigDecimal getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }

    public String getPaymentSN() {
        return paymentSN;
    }

    public void setPaymentSN(String paymentSN) {
        this.paymentSN = paymentSN;
    }
}
