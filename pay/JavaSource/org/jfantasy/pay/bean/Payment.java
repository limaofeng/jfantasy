package org.jfantasy.pay.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.pay.order.entity.enums.PaymentStatus;
import org.jfantasy.pay.order.entity.enums.PaymentType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付记录
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-12-5 上午9:22:59
 */
@ApiModel(value = "支付记录")
@Entity
@Table(name = "PAYMENT", uniqueConstraints = {@UniqueConstraint(columnNames = {"PAY_CONFIG_ID", "ORDER_TYPE", "ORDER_SN", "PAY_STATUS"})})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Payment extends BaseBusEntity {

    private static final long serialVersionUID = 6404772131152718534L;

    /**
     * 支付编号
     */
    @Id
    @ApiModelProperty("支付编号")
    @Column(name = "SN", updatable = false)
    @GeneratedValue(generator = "serialnumber")
    @GenericGenerator(name = "serialnumber", strategy = "serialnumber", parameters = {@Parameter(name = "expression", value = "'P' + #DateUtil.format('yyyyMMdd') + #StringUtil.addZeroLeft(#SequenceInfo.nextValue('PATMENT-SN' + #DateUtil.format('yyyyMMdd')), 5)")})
    private String sn;
    /**
     * 交易号（用于记录第三方交易的交易流水号）
     */
    @ApiModelProperty(value = "交易号", notes = "用于记录第三方交易的交易流水号")
    @Column(name = "TRADE_NO")
    private String tradeNo;
    /**
     * 支付类型
     */
    @ApiModelProperty("支付类型")
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", nullable = false, updatable = false)
    private PaymentType type;
    /**
     * 支付配置名称
     */
    @ApiModelProperty("支付配置名称")
    @Column(name = "PAYMENT_CONFIG_NAME", nullable = false, updatable = false)
    private String payConfigName;
    /**
     * 收款银行名称
     */
    @ApiModelProperty(value = "收款方名称", notes = "在线支付时为支付渠道名称")
    @Column(name = "BANK_NAME", updatable = false)
    private String bankName;
    /**
     * 收款银行账号
     */
    @ApiModelProperty(value = "收款账号", notes = "在线支付时为第三方支付账户")
    @Column(name = "BANK_ACCOUNT", updatable = false)
    private String bankAccount;
    /**
     * 支付金额
     */
    @ApiModelProperty("支付金额")
    @Column(name = "TOTAL_AMOUNT", nullable = false, updatable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount;
    /**
     * 支付手续费
     */
    @ApiModelProperty("支付手续费")
    @Column(name = "PAYMENT_FEE", nullable = false, updatable = false, precision = 15, scale = 2)
    private BigDecimal paymentFee;
    /**
     * 付款人
     */
    @ApiModelProperty("付款人")
    @Column(name = "PAYER", updatable = false)
    private String payer;
    /**
     * 备注
     */
    @ApiModelProperty("备注")
    @Column(name = "MEMO", updatable = false, length = 3000)
    private String memo;
    /**
     * 支付状态
     */
    @ApiModelProperty("支付状态")
    @Enumerated(EnumType.STRING)
    @Column(name = "PAY_STATUS", nullable = false)
    private PaymentStatus status;
    /**
     * 支付方式
     */
    @ApiModelProperty("支付方式")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAY_CONFIG_ID", foreignKey = @ForeignKey(name = "FK_PAYMENT_PAYMENT_CONFIG"))
    private PayConfig payConfig;
    /**
     * 订单详情
     */
    @ApiModelProperty("订单详情")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(value = {@JoinColumn(name = "ORDER_TYPE", referencedColumnName = "TYPE"), @JoinColumn(name = "ORDER_SN", referencedColumnName = "SN")})
    private Order order;
    /**
     * 支付时间
     */
    @ApiModelProperty(value = "支付时间", notes = "用于记录第三方交易的交易时间")
    @Column(name = "TRADE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date tradeTime;

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

    public PayConfig getPayConfig() {
        return payConfig;
    }

    public void setPayConfig(PayConfig payConfig) {
        this.payConfig = payConfig;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Date getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(Date tradeTime) {
        this.tradeTime = tradeTime;
    }

    @Transient
    public String getOrderKey() {
        return this.getOrder().getKey();
    }

}