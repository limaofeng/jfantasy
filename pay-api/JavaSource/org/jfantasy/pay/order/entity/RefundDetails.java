package org.jfantasy.pay.order.entity;

import io.swagger.annotations.ApiModelProperty;
import org.jfantasy.pay.order.entity.enums.RefundStatus;
import org.jfantasy.pay.order.entity.enums.RefundType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 退款
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-12-5 上午9:22:39
 */
public class RefundDetails implements Serializable {

    private static final long serialVersionUID = -2533117666249761057L;

    public RefundDetails() {
    }

    private OrderKey orderKey;
    private String sn;// 退款编号
    private RefundType type;// 退款类型
    @ApiModelProperty("退款状态")
    private RefundStatus status;
    /**
     * 支付配置标示
     */
    @ApiModelProperty("支付配置Id")
    private Long payConfigId;
    @ApiModelProperty("支付配置名称")
    private String payConfigName;// 支付配置名称
    @ApiModelProperty("退款银行名称")
    private String bankName;// 退款银行名称
    @ApiModelProperty("退款银行账号")
    private String bankAccount;// 退款银行账号
    @ApiModelProperty("退款金额")
    private BigDecimal totalAmount;// 退款金额
    @ApiModelProperty("收款人")
    private String payee;// 收款人
    @ApiModelProperty("备注")
    private String memo;// 备注
    /**
     * 交易号（用于记录第三方交易的交易流水号）
     */
    @ApiModelProperty(value = "交易号", notes = "用于记录第三方交易的交易流水号")
    private String tradeNo;
    @ApiModelProperty(value = "支付时间",notes = "用于记录第三方交易的交易时间")
    private Date tradeTime;
    /**
     * 原支付交易
     */
    @ApiModelProperty("原支付交易")
    private PaymentDetails payment;
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

    public String getCreator() {
        return this.creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getModifier() {
        return this.modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public RefundType getType() {
        return type;
    }

    public void setType(RefundType type) {
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

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Long getPayConfigId() {
        return payConfigId;
    }

    public void setPayConfigId(Long payConfigId) {
        this.payConfigId = payConfigId;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public RefundStatus getStatus() {
        return status;
    }

    public void setStatus(RefundStatus status) {
        this.status = status;
    }

    public PaymentDetails getPayment() {
        return payment;
    }

    public void setPayment(PaymentDetails payment) {
        this.payment = payment;
    }

    public OrderKey getOrderKey() {
        return orderKey;
    }

    public void setOrderKey(OrderKey orderKey) {
        this.orderKey = orderKey;
    }
}