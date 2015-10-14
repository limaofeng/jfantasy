package com.fantasy.payment.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import com.fantasy.framework.util.jackson.JSON;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 支付配置
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-12-5 上午9:22:49
 */
@ApiModel("支付配置")
@Entity
@Table(name = "MALL_PAYMENT_CONFIG")
@JsonFilter(JSON.CUSTOM_FILTER)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "orders", "payments", "refunds"})
public class PaymentConfig extends BaseBusEntity {

    private static final long serialVersionUID = -7950849648189504426L;

    // 支付配置类型（线下支付、在线支付）
    public enum PaymentConfigType {
        offline, online
    }

    // 支付手续费类型（按比例收费、固定费用）
    public enum PaymentFeeType {
        scale, fixed
    }

    @Id
    @Column(name = "ID", insertable = true, updatable = false)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 支付配置类型
     */
    @ApiModelProperty("支付配置类型")
    @Enumerated(EnumType.STRING)
    @Column(name = "PAYMENT_CONFIG_TYPE", nullable = false, updatable = false)
    private PaymentConfigType paymentConfigType;
    /**
     * 支付方式名称
     */
    @ApiModelProperty("支付方式名称")
    @Column(name = "NAME", nullable = false)
    private String name;
    /**
     * 支付产品标识
     */
    @ApiModelProperty("支付产品标识")
    @Column(name = "PAYMENT_PRODUCT_ID", updatable = false)
    private String paymentProductId;
    /**
     * 商家ID
     */
    @ApiModelProperty("商家ID")
    @Column(name = "BARGAINOR_ID")
    private String bargainorId;
    /**
     * 商户私钥
     */
    @ApiModelProperty("商户私钥")
    @Column(name = "BARGAINOR_KEY")
    private String bargainorKey;
    /**
     * 担保支付的卖家 email
     */
    @ApiModelProperty("担保支付的卖家 email")
    @Column(name = "SELLER_EMAIL")
    private String sellerEmail;
    /**
     * 支付手续费类型
     */
    @ApiModelProperty("支付手续费类型")
    @Enumerated(EnumType.STRING)
    @Column(name = "PAYMENT_FEE_TYPE", nullable = false)
    private PaymentFeeType paymentFeeType;
    /**
     * 支付费用
     */
    @ApiModelProperty("支付费用")
    @Column(name = "PAYMENT_FEE", nullable = false, precision = 15, scale = 5)
    private BigDecimal paymentFee;
    /**
     * 介绍
     */
    @ApiModelProperty("介绍")
    @Column(name = "DESCRIPTION", length = 3000)
    private String description;
    /**
     * 排序
     */
    @ApiModelProperty("排序")
    @Column(name = "SORT")
    private Integer sort;
    @ApiModelProperty(hidden = true)
    @OneToMany(mappedBy = "paymentConfig", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    private List<Payment> payments = new ArrayList<Payment>();// 支付
    @ApiModelProperty(hidden = true)
    @OneToMany(mappedBy = "paymentConfig", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    private List<Refund> refunds = new ArrayList<Refund>();// 退款

    public PaymentConfig() {
    }

    public PaymentConfig(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PaymentConfigType getPaymentConfigType() {
        return paymentConfigType;
    }

    public void setPaymentConfigType(PaymentConfigType paymentConfigType) {
        this.paymentConfigType = paymentConfigType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PaymentFeeType getPaymentFeeType() {
        return paymentFeeType;
    }

    public void setPaymentFeeType(PaymentFeeType paymentFeeType) {
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

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public List<Refund> getRefunds() {
        return refunds;
    }

    public String getBargainorId() {
        return bargainorId;
    }

    public void setBargainorId(String bargainorId) {
        this.bargainorId = bargainorId;
    }

    public String getBargainorKey() {
        return bargainorKey;
    }

    public void setBargainorKey(String bargainorKey) {
        this.bargainorKey = bargainorKey;
    }

    public void setRefunds(List<Refund> refunds) {
        this.refunds = refunds;
    }

    public String getPaymentProductId() {
        return paymentProductId;
    }

    public void setPaymentProductId(String paymentProductId) {
        this.paymentProductId = paymentProductId;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    /**
     * 根据总金额计算支付费用
     *
     * @return 支付费用
     */
    @Transient
    public BigDecimal getPaymentFee(BigDecimal totalAmount) {
        BigDecimal paymentFee;
        if (paymentFeeType == PaymentFeeType.scale) {
            paymentFee = totalAmount.multiply(this.paymentFee.divide(new BigDecimal(100)));
        } else {
            paymentFee = this.paymentFee;
        }
        return paymentFee;
    }

}