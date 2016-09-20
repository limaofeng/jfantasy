package org.jfantasy.member.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.spring.validation.RESTful;

import javax.persistence.*;
import javax.validation.constraints.Null;
import java.math.BigDecimal;

/**
 * 开票订单
 */
@Entity
@Table(name = "MEM_INVOICE_ORDER")
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler", "status"})
public class InvoiceOrder extends BaseBusEntity {

    private static final long serialVersionUID = -5904526950993808065L;

    public enum InvoiceOrderStatus {
        /**
         * 未开票
         */
        NONE,
        /**
         * 开票中
         */
        IN_PROGRESS,
        /**
         * 已完成
         */
        COMPLETE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "invoice_order_gen")
    @TableGenerator(name = "invoice_order_gen", table = "sys_sequence", pkColumnName = "gen_name", pkColumnValue = "mem_invoice_order:id", valueColumnName = "gen_value")
    @Column(name = "ID", updatable = false)
    private Long id;
    /**
     * 订单编号
     */
    @Column(name = "ORDER_SN")
    private String orderSn;
    /**
     * 订单类型
     */
    @Column(name = "ORDER_TYPE")
    private String orderType;
    /**
     * 订单描述
     */
    @Column(name = "NAME")
    private String name;
    /**
     * 实际金额(订单金额)
     */
    @Column(name = "REAL_AMOUNT")
    private BigDecimal realAmount;
    /**
     * 开票金额
     */
    @Column(name = "INVOICE_AMOUNT")
    private BigDecimal invoiceAmount;
    /**
     * 状态
     */
    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private InvoiceOrderStatus status;
    /**
     * 订单对应的用户
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", foreignKey = @ForeignKey(name = "FK_MEM_INVOICE_ORDER_MEMBER"))
    private Member member;
    /*************************************/
    /*             开票方                 */
    /*************************************/
    @Null(groups = {RESTful.POST.class})
    @Column(name = "TARGET_TYPE", nullable = false, updatable = false, length = 10)
    private String targetType;
    @Null(groups = {RESTful.POST.class})
    @Column(name = "TARGET_ID", nullable = false, updatable = false, length = 20)
    private String targetId;

    public InvoiceOrder() {
    }

    public InvoiceOrder(Long id) {
        this.setId(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public InvoiceOrderStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceOrderStatus status) {
        this.status = status;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    @Transient
    public Long getMemberId() {
        return member == null ? null : member.getId();
    }

    public void setMemberId(Long memberId) {
        this.member = new Member(memberId);
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
