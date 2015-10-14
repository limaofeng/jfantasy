package com.fantasy.mall.delivery.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import com.fantasy.framework.util.jackson.JSON;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "MALL_DELIVERY_RESHIP")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonFilter(JSON.CUSTOM_FILTER)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Reship extends BaseBusEntity {

    private static final long serialVersionUID = 4439185740262484180L;
    @Id
    @Column(name = "ID", insertable = true, updatable = false)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    @Column(name = "SN", nullable = false, unique = true)
    @GenericGenerator(name = "serialnumber", strategy = "serialnumber", parameters = {@Parameter(name = "expression", value = "'SN_' + #DateUtil.format('yyyyMMdd') + #StringUtil.addZeroLeft(#SequenceInfo.nextValue('RESHIP-SN'), 5)")})
    private String sn;// 退货编号
    @Column(name = "DELIVERY_TYPE_NAME", length = 50)
    private String deliveryTypeName;// 配送方式名称
    @Column(name = "DELIVERY_CORP_NAME", length = 50, nullable = false, updatable = false)
    private String deliveryCorpName; // 物流公司名称
    @Column(name = "DELIVERY_SN", length = 50, updatable = false)
    private String deliverySn;// 物流单号
    @Column(name = "DELIVERY_FEE", precision = 10, scale = 2, nullable = false, updatable = false)
    private BigDecimal deliveryFee;// 物流费用
    @Column(name = "RESHIP_NAME", length = 50, nullable = false, updatable = false)
    private String reshipName;// 退货人姓名
    @Column(name = "RESHIP_AREA_STORE", length = 300, nullable = false, updatable = false)
    private String reshipAreaStore;// 退货地区存储
    @Column(name = "RESHIP_ADDRESS", length = 150, nullable = false, updatable = false)
    private String reshipAddress;// 退货地址
    @Column(name = "RESHIP_ZIP_CODE", length = 10, nullable = false, updatable = false)
    private String reshipZipCode;// 退货邮编
    @Column(name = "RESHIP_PHONE", length = 12, updatable = false)
    private String reshipPhone;// 退货电话
    @Column(name = "RESHIP_MOBILE", length = 12, updatable = false)
    private String reshipMobile;// 退货手机
    @Column(name = "memo", length = 150, updatable = false)
    private String memo;// 备注
    @Column(name = "ORDER_TYPE", length = 20)
    private String orderType;
    @Column(name = "ORDER_SN")
    private String orderSn;// 订单
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DELIVERY_TYPE_ID",foreignKey = @ForeignKey(name = "FK_RESHIP_DELIVERY_TYPE") )

    private DeliveryType deliveryType;// 配送方式

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getDeliveryTypeName() {
        return deliveryTypeName;
    }

    public void setDeliveryTypeName(String deliveryTypeName) {
        this.deliveryTypeName = deliveryTypeName;
    }

    public String getDeliveryCorpName() {
        return deliveryCorpName;
    }

    public void setDeliveryCorpName(String deliveryCorpName) {
        this.deliveryCorpName = deliveryCorpName;
    }

    public String getDeliverySn() {
        return deliverySn;
    }

    public void setDeliverySn(String deliverySn) {
        this.deliverySn = deliverySn;
    }

    public BigDecimal getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(BigDecimal deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public String getReshipName() {
        return reshipName;
    }

    public void setReshipName(String reshipName) {
        this.reshipName = reshipName;
    }

    public String getReshipAreaStore() {
        return reshipAreaStore;
    }

    public void setReshipAreaStore(String reshipAreaStore) {
        this.reshipAreaStore = reshipAreaStore;
    }

    public String getReshipAddress() {
        return reshipAddress;
    }

    public void setReshipAddress(String reshipAddress) {
        this.reshipAddress = reshipAddress;
    }

    public String getReshipZipCode() {
        return reshipZipCode;
    }

    public void setReshipZipCode(String reshipZipCode) {
        this.reshipZipCode = reshipZipCode;
    }

    public String getReshipPhone() {
        return reshipPhone;
    }

    public void setReshipPhone(String reshipPhone) {
        this.reshipPhone = reshipPhone;
    }

    public String getReshipMobile() {
        return reshipMobile;
    }

    public void setReshipMobile(String reshipMobile) {
        this.reshipMobile = reshipMobile;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public DeliveryType getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(DeliveryType deliveryType) {
        this.deliveryType = deliveryType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}