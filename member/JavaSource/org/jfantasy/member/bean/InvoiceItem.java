package org.jfantasy.member.bean;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.spring.validation.RESTful;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "MEM_INVOICE_ITEM")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "invoice", "id"})
public class InvoiceItem extends BaseBusEntity {

    @Id
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    @Column(name = "ID", updatable = false)
    private Long id;
    /**
     * 发票
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INVOICE_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_MEM_INVOICE_ITEM"))
    private Invoice invoice;
    /**
     * 开票订单
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INVOICE_ORDER_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_ORDER_ITEM_ORDER"))
    private InvoiceOrder order;// 订单

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    @JsonUnwrapped
    public InvoiceOrder getOrder() {
        return order;
    }

    public void setOrder(InvoiceOrder order) {
        this.order = order;
    }

    @JsonProperty("order_id")
    @Transient
    @NotNull(groups = {RESTful.POST.class})
    public Long getOrderId() {
        return this.getOrder() != null ? this.order.getId() : null;
    }

    @JsonProperty("order_id")
    @Transient
    public void setOrderId(Long orderId) {
        this.setOrder(new InvoiceOrder(orderId));
    }

}
