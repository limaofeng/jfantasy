package org.jfantasy.pay.bean;

import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.pay.product.PayType;

import javax.persistence.*;

//@Table(name = "PAY_CONFIG")
public class PayLog extends BaseBusEntity {

    public enum Type {
        /**
         * 第三方支付发过来的消息或者回复的报文
         */
        in,
        /**
         * 发出去的报文
         */
        out;

    }

    @Id
    @Column(name = "ID", insertable = true, updatable = false)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 支付产品
     */
    @Column(name = "PAY_PRODUCT_ID", updatable = false)
    private String payProductId;
    /**
     * 支付产品
     */
    @Column(name = "PAY_CONFIG_ID", updatable = false)
    private PayConfig payConfig;
    /**
     * 支付详情
     */
    @Column(name = "PAYMENT_ID", updatable = false)
    private String Payment;
    /**
     * 报文类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", length = 20)
    private Type type;
    /**
     * 交易类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "PAY_TYPE", length = 20)
    private PayType payType;
    /**
     * 报文主体
     */
    @Column(name = "BODY", length = 2000)
    private String body;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPayProductId() {
        return payProductId;
    }

    public void setPayProductId(String payProductId) {
        this.payProductId = payProductId;
    }

    public String getPayment() {
        return Payment;
    }

    public void setPayment(String payment) {
        Payment = payment;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public PayType getPayType() {
        return payType;
    }

    public void setPayType(PayType payType) {
        this.payType = payType;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
