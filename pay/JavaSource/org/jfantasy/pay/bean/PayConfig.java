package org.jfantasy.pay.bean;

import org.jfantasy.file.bean.FileDetail;
import org.jfantasy.file.bean.converter.FileDetailConverter;
import org.jfantasy.file.bean.databind.FileDetailDeserializer;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.util.jackson.JSON;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
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
@Table(name = "PAY_CONFIG")
@JsonFilter(JSON.CUSTOM_FILTER)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "payments", "refunds"})
public class PayConfig extends BaseBusEntity {

    private static final long serialVersionUID = -7950849648189504426L;

    // 支付配置类型（线下支付、在线支付）
    public enum PayConfigType {
        offline, online
    }

    // 支付手续费类型（按比例收费、固定费用）
    public enum PayFeeType {
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
    @Column(name = "PAY_CONFIG_TYPE", nullable = false, updatable = false)
    private PayConfigType payConfigType;
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
    @Column(name = "PAY_PRODUCT_ID", updatable = false)
    private String payProductId;
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
     * 签名证书(银联支付专用)
     */
    @Column(name = "SIGN_CERT", length = 500)
    @Convert(converter = FileDetailConverter.class)
    private FileDetail signCert;
    /**
     * 加密证书(银联支付专用)
     */
    @Column(name = "ENCRYPT_CERT", length = 500)
    @Convert(converter = FileDetailConverter.class)
    private FileDetail encryptCert;
    /**
     * 签名验证证书(银联支付专用)
     */
    @Column(name = "VALIDATE_CERT",length = 500)
    @Convert(converter = FileDetailConverter.class)
    private FileDetail validateCert;
    /**
     * 支付手续费类型
     */
    @ApiModelProperty("支付手续费类型")
    @Enumerated(EnumType.STRING)
    @Column(name = "PAY_FEE_TYPE", nullable = false)
    private PayFeeType payFeeType;
    /**
     * 支付费用
     */
    @ApiModelProperty("支付费用")
    @Column(name = "PAY_FEE", nullable = false, precision = 15, scale = 5)
    private BigDecimal payFee;
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
    @OneToMany(mappedBy = "payConfig", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    private List<Payment> payments = new ArrayList<Payment>();// 支付
    @ApiModelProperty(hidden = true)
    @OneToMany(mappedBy = "payConfig", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    private List<Refund> refunds = new ArrayList<Refund>();// 退款

    public PayConfig() {
    }

    public PayConfig(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PayConfigType getPayConfigType() {
        return payConfigType;
    }

    public void setPayConfigType(PayConfigType paymentConfigType) {
        this.payConfigType = paymentConfigType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PayFeeType getPayFeeType() {
        return payFeeType;
    }

    public void setPayFeeType(PayFeeType payFeeType) {
        this.payFeeType = payFeeType;
    }

    public BigDecimal getPayFee() {
        return payFee;
    }

    public void setPayFee(BigDecimal payFee) {
        this.payFee = payFee;
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

    public String getPayProductId() {
        return payProductId;
    }

    public void setPayProductId(String payProductId) {
        this.payProductId = payProductId;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    public FileDetail getSignCert() {
        return signCert;
    }

    @JsonDeserialize(using = FileDetailDeserializer.class)
    public void setSignCert(FileDetail signCert) {
        this.signCert = signCert;
    }

    public FileDetail getEncryptCert() {
        return encryptCert;
    }

    @JsonDeserialize(using = FileDetailDeserializer.class)
    public void setEncryptCert(FileDetail encryptCert) {
        this.encryptCert = encryptCert;
    }

    public FileDetail getValidateCert() {
        return validateCert;
    }

    @JsonDeserialize(using = FileDetailDeserializer.class)
    public void setValidateCert(FileDetail validateCert) {
        this.validateCert = validateCert;
    }
}