package org.jfantasy.pay.bean;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;

@Table(name = "PAY_Log")
public class PayLog extends BaseBusEntity {

    public enum LogType {
        payment, refund
    }

    @Id
    @Column(name = "ID", updatable = false)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 日志类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", updatable = false)
    private LogType type;
    /**
     * 目标ID (支付或者退款)
     */
    @Column(name = "TARGET_ID", updatable = false)
    private String targetId;
    /**
     * 消息内容
     */
    @Column(name = "BODY", length = 200)
    private String body;
    /**
     * 订单详情
     */
    @ApiModelProperty("订单详情")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(value = {@JoinColumn(name = "ORDER_TYPE", referencedColumnName = "TYPE"), @JoinColumn(name = "ORDER_SN", referencedColumnName = "SN")})
    private Order order;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

}
