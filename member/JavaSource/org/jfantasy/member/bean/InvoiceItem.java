package org.jfantasy.member.bean;


import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;

public class InvoiceItem extends BaseBusEntity {

    @Id
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    @Column(name = "ID", insertable = true, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID", nullable = false,foreignKey =@ForeignKey(name = "FK_ORDER_ITEM_ORDER") )
    private Invoice invoice;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INVOICE_ORDER_ID", nullable = false,foreignKey =@ForeignKey(name = "FK_ORDER_ITEM_ORDER") )
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

    public InvoiceOrder getOrder() {
        return order;
    }

    public void setOrder(InvoiceOrder order) {
        this.order = order;
    }

}
