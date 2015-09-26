package com.fantasy.mall.delivery.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.mall.delivery.bean.databind.DeliveryCorpDeserializer;
import com.fantasy.mall.delivery.bean.databind.DeliveryCorpSerializer;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.CacheConcurrencyStrategy;
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
@ApiModel("配送方式")
@Entity
@Table(name = "MALL_DELIVERY_TYPE")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonFilter(JSON.CUSTOM_FILTER)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "shippings", "reships"})
public class DeliveryType extends BaseBusEntity {

    private static final long serialVersionUID = 5873163245980853245L;

    public DeliveryType() {
    }

    public DeliveryType(Long id) {
        this.id = id;
    }

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
    @ApiModelProperty("配送方式名称")
    @Column(name = "NAME", nullable = false)
    private String name;
    @ApiModelProperty("配送类型")
    @Enumerated(EnumType.STRING)
    @Column(name = "DELIVERY_METHOD", length = 50, nullable = false)
    private DeliveryMethod method;
    @ApiModelProperty("首重量(单位: 克)")
    @Column(name = "first_Weight", nullable = false)
    private Integer firstWeight;
    @ApiModelProperty("续重量(单位: 克)")
    @Column(name = "CONTINUE_WEIGHT", nullable = false)
    private Integer continueWeight;
    @ApiModelProperty("首重价格")
    @Column(name = "FIRST_WEIGHT_PRICE", precision = 15, scale = 5, nullable = false)
    private BigDecimal firstWeightPrice;
    @ApiModelProperty("续重价格")
    @Column(name = "CONTINUE_WEIGHT_PRICE", precision = 15, scale = 5, nullable = false)
    private BigDecimal continueWeightPrice;
    @ApiModelProperty("介绍")
    @Column(name = "DESCRIPTION", length = 3000)
    private String description;
    @ApiModelProperty(value = "默认物流公司")
    @JsonProperty("corpId")
    @JsonDeserialize(using = DeliveryCorpDeserializer.class)
    @JsonSerialize(using = DeliveryCorpSerializer.class)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "DELIVERY_CORP_ID", foreignKey = @ForeignKey(name = "MALL_DELIVERY_TYPE_CORP"))
    private DeliveryCorp defaultDeliveryCorp;
    @ApiModelProperty(value = "发货", hidden = true)
    @OneToMany(mappedBy = "deliveryType", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Shipping> shippings;
    @ApiModelProperty(value = "退货", hidden = true)
    @OneToMany(mappedBy = "deliveryType", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Reship> reships;

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