package org.jfantasy.pay.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import com.fantasy.framework.util.jackson.JSON;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 退款
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-12-5 上午9:22:39
 */
@Entity
@Table(name = "MALL_PAYMENT_REFUND")
@JsonFilter(JSON.CUSTOM_FILTER)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Refund extends BaseBusEntity {

    private static final long serialVersionUID = -2533117666249761057L;

    // 退款类型（在线支付、线下支付）
    public enum RefundType {
        online, offline
    }

    @Id
    @Column(name = "ID", insertable = true, updatable = false)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    @Column(name = "SN", nullable = false, updatable = false, unique = true)
    private String sn;// 退款编号
    @Enumerated(EnumType.STRING)
    @Column(name = "REFUND_TYPE", nullable = false, updatable = false)
    private RefundType refundType;// 退款类型
    @Column(name = "PAYMENT_CONFIG_NAME", nullable = false, updatable = false)
    private String paymentConfigName;// 支付配置名称
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
    @JoinColumn(name = "PAYMENT_CONFIG_ID", foreignKey = @ForeignKey(name = "FK_REFUND_PAYMENT_CONFIG"))
    private PayConfig payConfig;// 支付配置
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

    public RefundType getRefundType() {
        return refundType;
    }

    public void setRefundType(RefundType refundType) {
        this.refundType = refundType;
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
}