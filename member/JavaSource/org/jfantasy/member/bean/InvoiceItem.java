package org.jfantasy.member.bean;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.spring.validation.RESTful;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "MEM_INVOICE_ITEM")
@TableGenerator(name = "invoice_item_gen", table = "sys_sequence", pkColumnName = "gen_name", pkColumnValue = "mem_invoice_item:id", valueColumnName = "gen_value")
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler", "invoice", "id"})
public class InvoiceItem extends BaseBusEntity {

    private static final long serialVersionUID = -2533133703787196057L;
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "invoice_item_gen")
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
    @NotNull(groups = {RESTful.POST.class})
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
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public InvoiceOrder getOrder() {
        return order;
    }

    public void setOrder(InvoiceOrder order) {
        this.order = order;
    }

    @JsonProperty("order_id")
    @Transient
    public void setOrderId(Long orderId) {
        this.setOrder(new InvoiceOrder(orderId));
    }

}
