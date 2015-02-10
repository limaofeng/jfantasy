package com.fantasy.payment.ws.dto;

import java.math.BigDecimal;

/**
 * 支付配置 DTO
 */
public class PaymentConfigDTO {

    private Long id;
    /**
     * 支付配置类型（线下支付、在线支付）offline, online
     */
    private String paymentConfigType;
    /**
     * 支付方式名称
     */
    private String name;
    /**
     * 支付产品标识
     */
    private String paymentProductId;
    /**
     * 支付手续费类型（按比例收费、固定费用）scale, fixed
     */
    private String paymentFeeType;
    /**
     * 支付费用
     */
    private BigDecimal paymentFee;
    /**
     * 介绍
     */
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPaymentConfigType() {
        return paymentConfigType;
    }

    public void setPaymentConfigType(String paymentConfigType) {
        this.paymentConfigType = paymentConfigType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPaymentProductId() {
        return paymentProductId;
    }

    public void setPaymentProductId(String paymentProductId) {
        this.paymentProductId = paymentProductId;
    }

    public String getPaymentFeeType() {
        return paymentFeeType;
    }

    public void setPaymentFeeType(String paymentFeeType) {
        this.paymentFeeType = paymentFeeType;
    }

    public BigDecimal getPaymentFee() {
        return paymentFee;
    }

    public void setPaymentFee(BigDecimal paymentFee) {
        this.paymentFee = paymentFee;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
