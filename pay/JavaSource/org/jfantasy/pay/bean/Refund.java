package org.jfantasy.pay.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.pay.order.entity.enums.PaymentType;
import org.jfantasy.pay.order.entity.enums.RefundStatus;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 退款
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-12-5 上午9:22:39
 */
@Entity
@Table(name = "REFUND")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Refund extends BaseBusEntity {

    private static final long serialVersionUID = -2533117666249761057L;

    public Refund() {
    }

    public Refund(Payment payment) {
        this.setType(payment.getType());
        this.setStatus(RefundStatus.ready);
        this.setBankAccount(payment.getBankAccount());
        this.setBankName(payment.getBankName());
        this.setPayConfigName(payment.getPayConfigName());
        this.setPayConfig(payment.getPayConfig());
        this.setPayment(payment);
        this.setPayee(payment.getPayer());
        this.setOrder(payment.getOrder());
    }

    @Id
    @GeneratedValue(generator = "serialnumber")
    @GenericGenerator(name = "serialnumber", strategy = "serialnumber", parameters = {@org.hibernate.annotations.Parameter(name = "expression", value = "'R' + payment.sn + #StringUtil.addZeroLeft(#SequenceInfo.nextValue('REFUND-' + payment.sn), 2)")})
    @Column(name = "SN", updatable = false)
    private String sn;// 退款编号
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", updatable = false, nullable = false)
    private PaymentType type;// 退款类型
    @ApiModelProperty("退款状态")
    @Enumerated(EnumType.STRING)
    @Column(name = "PAY_STATUS", nullable = false)
    private RefundStatus status;
    @Column(name = "PAYMENT_CONFIG_NAME", nullable = false, updatable = false)
    private String payConfigName;// 支付配置名称
    @Column(name = "BANK_NAME", updatable = false)
    private String bankName;// 退款银行名称
    @Column(name = "BANK_ACCOUNT", updatable = false)
    private String bankAccount;// 退款银行账号
    @Column(name = "TOTAL_AMOUNT", nullable = false, updatable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount;// 退款金额
    @Column(name = "PAYEE", updatable = false)
    private String payee;// 收款人
    @Column(name = "MEMO", updatable = false, length = 3000)
    private String memo;// 备注
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAY_CONFIG_ID", updatable = false, foreignKey = @ForeignKey(name = "FK_REFUND_PAYMENT_CONFIG"))
    private PayConfig payConfig;
    /**
     * 交易号（用于记录第三方交易的交易流水号）
     */
    @ApiModelProperty(value = "交易号", notes = "用于记录第三方交易的交易流水号")
    @Column(name = "TRADE_NO")
    private String tradeNo;
    @ApiModelProperty(value = "交易时间", notes = "用于记录第三方交易的交易时间")
    @Column(name = "TRADE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date tradeTime;
    /**
     * 原支付交易
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAYMENT_ID", updatable = false, foreignKey = @ForeignKey(name = "FK_REFUND_PAYMENT"))
    private Payment payment;
    @ApiModelProperty("订单详情")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(value = {@JoinColumn(name = "ORDER_TYPE", referencedColumnName = "TYPE", updatable = false), @JoinColumn(name = "ORDER_SN", referencedColumnName = "SN", updatable = false)})
    private Order order;

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

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Order getOrder() {
        return order;
    }

    public Date getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(Date tradeTime) {
        this.tradeTime = tradeTime;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Transient
    public String getOrderKey() {
        return this.getOrder().getKey();
    }

}