package com.fantasy.payment.ws.dto;

import com.fantasy.member.ws.dto.MemberDTO;

import java.math.BigDecimal;

/**
 * 支付信息
 */
public class PaymentDTO {

    private Long id;
    /**
     * 支付编号
     */
    private String sn;
    /**
     * 交易号（用于记录第三方交易的交易流水号）
     */
    private String tradeNo;
    /**
     * 支付类型
     */
    private String paymentType;
    /**
     * 支付配置名称
     */
    private String paymentConfigName;
    /**
     * 收款银行名称
     */
    private String bankName;
    /**
     * 收款银行账号
     */
    private String bankAccount;
    /**
     * 支付金额
     */
    private BigDecimal totalAmount;
    /**
     * 支付手续费
     */
    private BigDecimal paymentFee;
    /**
     * 付款人
     */
    private String payer;
    /**
     * 备注
     */
    private String memo;
    /**
     * 支付状态
     */
    private String paymentStatus;
    /**
     * 会员
     */
    private MemberDTO member;
    /**
     * 支付方式
     */
    private PaymentConfigDTO paymentConfig;
    /**
     * 订单类型
     */
    private String orderType;
    /**
     * 订单编号
     */
    private String orderSn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentConfigName() {
        return paymentConfigName;
    }

    public void setPaymentConfigName(String paymentConfigName) {
        this.paymentConfigName = paymentConfigName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getPaymentFee() {
        return paymentFee;
    }

    public void setPaymentFee(BigDecimal paymentFee) {
        this.paymentFee = paymentFee;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public MemberDTO getMember() {
        return member;
    }

    public void setMember(MemberDTO member) {
        this.member = member;
    }

    public PaymentConfigDTO getPaymentConfig() {
        return paymentConfig;
    }

    public void setPaymentConfig(PaymentConfigDTO paymentConfig) {
        this.paymentConfig = paymentConfig;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }
}
