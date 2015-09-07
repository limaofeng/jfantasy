package com.fantasy.mall.delivery.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * 配送方式
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-9-16 下午3:45:17
 */
@Entity
@Table(name = "MALL_DELIVERY_TYPE")
@JsonIgnoreProperties({"orders", "shippings", "reships"})
public class DeliveryType extends BaseBusEntity {

    private static final long serialVersionUID = 5873163245980853245L;

    // 配送类型：先付款后发货、货到付款
    public enum DeliveryMethod {
        /**
         * 先付款后发货
         */
        deliveryAgainstPayment,
        /**
         * 货到付款
         */
        cashOnDelivery
    }

    @Id
    @Column(name = "ID", insertable = true, updatable = false)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;// 配送方式名称
    @Enumerated(EnumType.STRING)
    @Column(name = "DELIVERY_METHOD", length = 50, nullable = false)
    private DeliveryMethod method;// 配送类型
    @Column(name = "first_Weight", nullable = false)
    private Integer firstWeight;// 首重量(单位: 克)
    @Column(name = "CONTINUE_WEIGHT", nullable = false)
    private Integer continueWeight;// 续重量(单位: 克)
    @Column(name = "FIRST_WEIGHT_PRICE", precision = 15, scale = 5, nullable = false)
    private BigDecimal firstWeightPrice;// 首重价格
    @Column(name = "CONTINUE_WEIGHT_PRICE", precision = 15, scale = 5, nullable = false)
    private BigDecimal continueWeightPrice;// 续重价格
    @Column(name = "DESCRIPTION", length = 3000)
    private String description;// 介绍

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "DELIVERY_CORP_ID", foreignKey = @ForeignKey(name = "MALL_DELIVERY_TYPE_CORP"))
    private DeliveryCorp defaultDeliveryCorp; // 默认物流公司

    @OneToMany(mappedBy = "deliveryType", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Shipping> shippings;// 发货
    @OneToMany(mappedBy = "deliveryType", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Reship> reships;// 退货

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DeliveryMethod getMethod() {
        return method;
    }

    public void setMethod(DeliveryMethod method) {
        this.method = method;
    }

    public Integer getFirstWeight() {
        return firstWeight;
    }

    public void setFirstWeight(Integer firstWeight) {
        this.firstWeight = firstWeight;
    }

    public Integer getContinueWeight() {
        return continueWeight;
    }

    public void setContinueWeight(Integer continueWeight) {
        this.continueWeight = continueWeight;
    }

    public BigDecimal getFirstWeightPrice() {
        return firstWeightPrice;
    }

    public void setFirstWeightPrice(BigDecimal firstWeightPrice) {
        this.firstWeightPrice = firstWeightPrice;
    }

    public BigDecimal getContinueWeightPrice() {
        return continueWeightPrice;
    }

    public void setContinueWeightPrice(BigDecimal continueWeightPrice) {
        this.continueWeightPrice = continueWeightPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Shipping> getShippings() {
        return shippings;
    }

    public void setShippings(List<Shipping> shippings) {
        this.shippings = shippings;
    }

    public List<Reship> getReships() {
        return reships;
    }

    public void setReships(List<Reship> reships) {
        this.reships = reships;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DeliveryCorp getDefaultDeliveryCorp() {
        return defaultDeliveryCorp;
    }

    public void setDefaultDeliveryCorp(DeliveryCorp defaultDeliveryCorp) {
        this.defaultDeliveryCorp = defaultDeliveryCorp;
    }

}