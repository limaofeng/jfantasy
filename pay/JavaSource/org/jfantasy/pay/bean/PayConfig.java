package org.jfantasy.pay.bean;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.jackson.ThreadJacksonMixInHolder;
import org.jfantasy.pay.bean.enums.PayMethod;
import org.jfantasy.pay.product.sign.Base64;

import javax.persistence.*;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 支付配置
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-12-5 上午9:22:49
 */
@ApiModel("支付配置")
@Entity
@Table(name = "PAY_PAYCONFIG")
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
    @Column(name = "ID", updatable = false)
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
     * 支付方式
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "PAY_METHOD", nullable = false, updatable = false)
    private PayMethod payMethod;
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
     * 针对不同支付平台的额外属性
     */
    @ApiModelProperty(hidden = true)
    @Column(name = "PROPERTIES", columnDefinition = "MediumBlob")
    private Properties properties;
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
    @Column(name = "PAY_FEE", nullable = false, precision = 15, scale = 2)
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
    /**
     * 支付记录
     */
    @ApiModelProperty(hidden = true)
    @OneToMany(mappedBy = "payConfig", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    private List<Payment> payments = new ArrayList<>();// 支付
    /**
     * 退款记录
     */
    @ApiModelProperty(hidden = true)
    @OneToMany(mappedBy = "payConfig", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    private List<Refund> refunds = new ArrayList<>();// 退款
    /**
     * 支持的平台(包含:app/web)
     */
    @Column(name = "PLATFORMS")
    private String platforms;
    /**
     * 是否为默认
     */
    @Transient
    private Boolean isDefault;

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

    @JsonAnyGetter
    public Properties getProperties() {
        if (ThreadJacksonMixInHolder.getMixInHolder().isIgnoreProperty(PayConfig.class, "properties")) {
            return null;
        }
        if (ThreadJacksonMixInHolder.isContainsMixIn()) {
            Properties newProperties = new Properties();
            for (Object _key : properties.keySet()) {
                String key = _key.toString();
                Object value = properties.get(key);
                if (value instanceof byte[]) {
                    newProperties.setProperty(key, "--Hidden Byte data--");
                } else if (value instanceof File) {
                    newProperties.setProperty(key, "--Hidden File data--");
                } else {
                    newProperties.setProperty(key, value.toString());
                }
            }
            return newProperties;
        }
        return properties;
    }

    @JsonAnySetter
    public void set(String key, Object value) {
        if (this.properties == null) {
            this.properties = new Properties();
        }
        this.properties.put(key, value);
    }

    @Transient
    public Object get(String key) {
        if (this.properties == null) return null;
        return this.properties.get(key);
    }

    @Transient
    public <T> T get(String key, Class<T> tClass) {
        if (this.properties == null) return null;
        Object value = this.properties.get(key);
        if (value == null) {
            return null;
        }
        if (tClass.isAssignableFrom(byte[].class) && value instanceof String) {
            return (T) Base64.decode(value.toString());
        }
        return tClass.cast(value);
    }

    public PayMethod getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(PayMethod payMethod) {
        this.payMethod = payMethod;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public String getPlatforms() {
        return platforms;
    }

    public void setPlatforms(String platforms) {
        this.platforms = platforms;
    }
}