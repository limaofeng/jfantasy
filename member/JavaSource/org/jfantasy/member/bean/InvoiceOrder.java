package org.jfantasy.member.bean;

import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * 开票订单
 */
public class InvoiceOrder extends BaseBusEntity {

    @Id
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    @Column(name = "ID", updatable = false)
    private Long id;

    @Column(name = "ORDER_SN")
    private String orderSn;

    @Column(name = "ORDER_TYPE")
    private String orderType;

    @Column(name = "NAME")
    private String name;

    @Column(name = "REAL_AMOUNT")
    private BigDecimal realAmount;

    @Column(name = "INVOICE_AMOUNT")
    private BigDecimal invoiceAmount;


}
