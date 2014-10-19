package com.fantasy.mall.order.bean;

import com.fantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;

/**
 * 订单日志
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-9-16 下午4:03:40
 */
public class OrderLog extends BaseBusEntity {

    private static final long serialVersionUID = -8602286694018650587L;

    // 订单日志类型（订单创建、订单修改、订单支付、订单退款、订单发货、订单退货、订单完成、订单作废）
    public enum OrderLogType {
        create, modify, payment, refund, shipping, reship, completed, invalid
    }

    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20, updatable = false)
    private OrderLogType orderLogType;// 订单日志类型
    @Column(nullable = false, updatable = false)
    private String orderSn;// 订单编号
    @Column(updatable = false)
    private String operator;// 操作员
    @Column(updatable = false, length = 3000)
    private String info;// 日志信息
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false,foreignKey =@ForeignKey(name = "FK_ORDER_LOG_ORDER") )

    private Order order;// 订单

    public OrderLogType getOrderLogType() {
        return orderLogType;
    }

    public void setOrderLogType(OrderLogType orderLogType) {
        this.orderLogType = orderLogType;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

}