package org.jfantasy.pay.order.entity;

import io.swagger.annotations.ApiModelProperty;
import org.jfantasy.pay.order.entity.enums.PaymentStatus;
import org.jfantasy.pay.order.entity.enums.PaymentType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付记录
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-12-5 上午9:22:59
 */
public class PaymentDetails implements Serializable {

    private static final long serialVersionUID = 6404772131152718534L;

    /**
     * 支付编号
     */
    @ApiModelProperty("支付编号")
    private String sn;
    /**
     * 交易号（用于记录第三方交易的交易流水号）
     */
    @ApiModelProperty(value = "交易号", notes = "用于记录第三方交易的交易流水号")
    private String tradeNo;

    @ApiModelProperty(value = "支付时间",notes = "用于记录第三方交易的交易时间")
    private Date tradeTime;
    /**
     * 支付类型
     */
    @ApiModelProperty("支付类型")
    private PaymentType type;
    /**
     * 支付配置标示
     */
    @ApiModelProperty("支付配置Id")
    private Long payConfigId;
    /**
     * 支付配置名称
     */
    @ApiModelProperty("支付配置名称")
    private String payConfigName;
    /**
     * 收款银行名称
     */
    @ApiModelProperty("收款方名称")
    private String bankName;
    /**
     * 收款银行账号
     */
    @ApiModelProperty("收款账号")
    private String bankAccount;
    /**
     * 支付金额
     */
    @ApiModelProperty("支付金额")
    private BigDecimal totalAmount;
    /**
     * 支付手续费
     */
    @ApiModelProperty("支付手续费")
    private BigDecimal paymentFee;
    /**
     * 付款人
     */
    @ApiModelProperty("付款人")
    private String payer;
    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String memo;
    /**
     * 支付状态
     */
    @ApiModelProperty("支付状态")
    private PaymentStatus status;
    /**
     * 创建人
     */
    @ApiModelProperty(hidden = true)
    private String creator;
    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Date createTime;
    /**
     * 最后修改人
     */
    @ApiModelProperty(hidden = true)
    private String modifier;
    /**
     * 最后修改时间
     */
    @ApiModelProperty(hidden = true)
    private Date modifyTime;

    private OrderKey orderKey;

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public PaymentType getType() {
        return type;
    }

    public void setType(PaymentType type) {
        this.type = type;
    }

    public String getPayConfigName() {
        return payConfigName;
    }

    public void setPayConfigName(String payConfigName) {
        this.payConfigName = payConfigName;
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

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
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

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public Long getPayConfigId() {
        return payConfigId;
    }

    public void setPayConfigId(Long payConfigId) {
        this.payConfigId = payConfigId;
    }

    public OrderKey getOrderKey() {
        return orderKey;
    }

    public void setOrderKey(OrderKey orderKey) {
        this.orderKey = orderKey;
    }

    public Date getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(Date tradeTime) {
        this.tradeTime = tradeTime;
    }

    public Date getCreateTime() {
        if (this.createTime == null) {
            return null;
        }
        return (Date) this.createTime.clone();
    }

    public void setCreateTime(Date createTime) {
        if (createTime == null) {
            this.createTime = null;
        } else {
            this.createTime = (Date) createTime.clone();
        }
    }

    public Date getModifyTime() {
        if (this.modifyTime == null) {
            return null;
        }
        return (Date) this.modifyTime.clone();
    }

    public void setModifyTime(Date modifyTime) {
        if (modifyTime == null) {
            this.modifyTime = null;
        } else {
            this.modifyTime = (Date) modifyTime.clone();
        }
    }
}