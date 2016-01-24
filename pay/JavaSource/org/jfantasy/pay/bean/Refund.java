package org.jfantasy.pay.bean;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.*;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.util.jackson.JSON;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 退款
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-12-5 上午9:22:39
 */
@Entity
@Table(name = "PAYMENT_REFUND")
@JsonFilter(JSON.CUSTOM_FILTER)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Refund extends BaseBusEntity {

    private static final long serialVersionUID = -2533117666249761057L;

    // 退款类型（在线支付、线下支付）
    public enum RefundType {
        online, offline
    }

    // 支付状态（准备、超时、作废、成功、失败）
    public enum Status {
        ready, invalid, success, failure
    }

    @Id
    @GeneratedValue(generator = "serialnumber")
    @GenericGenerator(name = "serialnumber", strategy = "serialnumber", parameters = {@org.hibernate.annotations.Parameter(name = "expression", value = "'R' + #sn")})
    @Column(name = "SN", updatable = false)
    private String sn;// 退款编号
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", nullable = false, updatable = false)
    private RefundType type;// 退款类型
    @ApiModelProperty("退款状态")
    @Enumerated(EnumType.STRING)
    @Column(name = "PAY_STATUS", nullable = false)
    private Status status;
    @Column(name = "PAYMENT_CONFIG_NAME", nullable = false, updatable = false)
    private String payConfigName;// 支付配置名称
    @Column(name = "BANK_NAME", updatable = false)
    private String bankName;// 退款银行名称
    @Column(name = "BANK_ACCOUNT", updatable = false)
    private String bankAccount;// 退款银行账号
    @Column(name = "TOTAL_AMOUNT", nullable = false, updatable = false, precision = 15, scale = 5)
    private BigDecimal totalAmount;// 退款金额
    @Column(name = "PAYEE", updatable = false)
    private String payee;// 收款人
    @Column(name = "MEMO", updatable = false, length = 3000)
    private String memo;// 备注
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAY_CONFIG_ID", foreignKey = @ForeignKey(name = "FK_REFUND_PAYMENT_CONFIG"))
    private PayConfig payConfig;// 支付配置
    /**
     * 交易号（用于记录第三方交易的交易流水号）
     */
    @ApiModelProperty(value = "交易号", notes = "用于记录第三方交易的交易流水号")
    @Column(name = "TRADE_NO", updatable = true)
    private String tradeNo;
    /**
     * 原支付交易
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAYMENT_ID", foreignKey = @ForeignKey(name = "FK_REFUND_PAYMENT"))
    private Payment payment;
    /**
     * 订单类型
     */
    @Column(name = "ORDER_TYPE")
    private String orderType;
    /**
     * 订单编号
     */
    @Column(name = "ORDER_SN")
    private String orderSn;

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

    public PayConfig getPayConfig() {
        return payConfig;
    }

    public void setPayConfig(PayConfig payConfig) {
        this.payConfig = payConfig;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}