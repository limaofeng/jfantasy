package org.jfantasy.pay.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.pay.bean.converter.OrderItemConverter;
import org.jfantasy.pay.order.entity.OrderItem;
import org.jfantasy.pay.order.entity.OrderKey;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ApiModel("订单详情")
@Entity
@IdClass(OrderKey.class)
@Table(name = "PAY_ORDER")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Order extends BaseBusEntity {

    // 付款状态（未支付、部分支付、已支付、部分退款、全额退款）
    public enum PaymentStatus {
        unpaid, partPayment, paid, partRefund, refunded
    }

    @ApiModelProperty("编号")
    @Id
    private String sn;
    @ApiModelProperty("订单类型")
    @Id
    private String type;
    @ApiModelProperty("支付状态")
    @Enumerated(EnumType.STRING)
    @Column(name = "PAYMENT_STATUS", length = 20, nullable = false)
    private PaymentStatus status;
    @ApiModelProperty("订单摘要")
    @Column(name = "subject", length = 250)
    private String subject;
    @ApiModelProperty("订单详情")
    @Column(name = "Body", length = 500)
    private String Body;
    @ApiModelProperty("订单总金额")
    @Column(name = "TOTAL_FEE", nullable = false, updatable = false, precision = 15, scale = 2)
    private BigDecimal totalFee;
    @ApiModelProperty("订单应付金额")
    @Column(name = "PAYABLE_FEE", nullable = false, updatable = false, precision = 15, scale = 2)
    private BigDecimal payableFee;
    @ApiModelProperty("支付方式")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAY_CONFIG_ID", foreignKey = @ForeignKey(name = "FK_ORDER_PAY_CONFIG"))
    private PayConfig payConfig;
    @ApiModelProperty("订单项")
    @Column(name = "ORDERITEM_STORE", length = 3000)
    @Convert(converter = OrderItemConverter.class)
    private List<OrderItem> orderItems;
    @ApiModelProperty("支付记录")
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    private List<Payment> payments = new ArrayList<Payment>();
    @ApiModelProperty("退款记录")
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    private List<Refund> refunds = new ArrayList<Refund>();
    @ApiModelProperty("付款时间")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PAYMENT_TIME")
    private Date paymentTime;

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return Body;
    }

    public void setBody(String body) {
        Body = body;
    }

    public BigDecimal getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }

    public BigDecimal getPayableFee() {
        return payableFee;
    }

    public void setPayableFee(BigDecimal payableFee) {
        this.payableFee = payableFee;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public PaymentStatus getStatus() {
        return this.status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public PayConfig getPayConfig() {
        return payConfig;
    }

    public void setPayConfig(PayConfig payConfig) {
        this.payConfig = payConfig;
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

    public void setRefunds(List<Refund> refunds) {
        this.refunds = refunds;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    @Transient
    public String getKey() {
        return OrderKey.newInstance(this.type, this.sn).toString();
    }

}
