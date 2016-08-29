package org.jfantasy.invoice;

import java.math.BigDecimal;

public class Order {
    /**
     * 订单编号
     */
    private String orderSn;
    /**
     * 订单类型
     */
    private String orderType;

    private String name;
    /**
     * 实际金额(订单金额)
     */
    private BigDecimal realAmount;
    /**
     * 开票金额
     */
    private BigDecimal invoiceAmount;
    /**
     * 会员ID
     */
    private Long memberId;
    /**
     * 开票方类型
     */
    private String targetType;
    /**
     * 开票方ID
     */
    private String targetId;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(BigDecimal realAmount) {
        this.realAmount = realAmount;
    }

    public BigDecimal getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

}
