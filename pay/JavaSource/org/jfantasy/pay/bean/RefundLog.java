package org.jfantasy.pay.bean;


import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.pay.order.entity.enums.RefundStatus;

import javax.persistence.*;
import java.util.Date;

@Table(name = "PAY_REFUND_LOG")
public class RefundLog extends BaseBusEntity {

    @Id
    @Column(name = "ID", updatable = false)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 退款信息表
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REFUND_SN", referencedColumnName = "SN", updatable = false)
    private Refund refund;
    /**
     * 订单详情
     */
    @ApiModelProperty("订单详情")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(value = {@JoinColumn(name = "ORDER_TYPE", referencedColumnName = "TYPE"), @JoinColumn(name = "ORDER_SN", referencedColumnName = "SN")})
    private Order order;
    /**
     * 支付状态
     */
    @ApiModelProperty("退款状态")
    @Enumerated(EnumType.STRING)
    @Column(name = "PAY_STATUS", nullable = false)
    private RefundStatus status;
    /**
     * 备注
     */
    @Column(name = "NOTES")
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

    public RefundStatus getStatus() {
        return status;
    }

    public void setStatus(RefundStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Refund getRefund() {
        return refund;
    }

    public void setRefund(Refund refund) {
        this.refund = refund;
    }

    /**
     * 交易时间
     */
    @ApiModelProperty("交易时间")
    public Date getTradeTime(){
        return this.getCreateTime();
    }

}
