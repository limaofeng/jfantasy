package org.jfantasy.pay.bean;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.pay.order.entity.enums.PaymentStatus;

import javax.persistence.*;
import java.util.Date;

@Table(name = "PAY_PAYMEN_LOG")
public class PaymentLog extends BaseBusEntity {

    @Id
    @Column(name = "ID", updatable = false)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 付款信息表
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAYMENT_SN", referencedColumnName = "SN", updatable = false)
    private Payment payment;
    /**
     * 订单详情
     */
    @ApiModelProperty("订单详情")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(value = {@JoinColumn(name = "ORDER_TYPE", referencedColumnName = "TYPE", updatable = false), @JoinColumn(name = "ORDER_SN", referencedColumnName = "SN", updatable = false)})
    private Order order;
    /**
     * 支付状态
     */
    @ApiModelProperty("支付状态")
    @Enumerated(EnumType.STRING)
    @Column(name = "PAY_STATUS", nullable = false, updatable = false)
    private PaymentStatus status;
    /**
     * 备注
     */
    @Column(name = "NOTES", updatable = false)
    private String notes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * 交易时间
     */
    @ApiModelProperty("交易时间")
    public Date getTradeTime() {
        return this.getCreateTime();
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}
