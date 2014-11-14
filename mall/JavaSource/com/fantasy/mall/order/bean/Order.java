package com.fantasy.mall.order.bean;

import com.fantasy.attr.DynaBean;
import com.fantasy.attr.bean.AttributeValue;
import com.fantasy.attr.bean.AttributeVersion;
import com.fantasy.common.bean.Area;
import com.fantasy.framework.dao.BaseBusEntity;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.mall.delivery.bean.DeliveryType;
import com.fantasy.mall.delivery.bean.Shipping;
import com.fantasy.member.bean.Member;
import com.fantasy.payment.bean.PaymentConfig;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;
import com.opensymphony.xwork2.util.Element;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 订单表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-9-21 下午4:21:42
 */
@Entity
@Table(name = "MALL_ORDER")
@JsonIgnoreProperties({"hibernateLazyInitializer", "shipAreaStore", "memeo", "shippings", "orderItems", "payments"})
public class Order extends BaseBusEntity implements DynaBean {

    private static final long serialVersionUID = -8541323033439515148L;

    public static final String PAYMENT_TYPE = "MALL_ORDER";

    // 订单状态（未处理、已处理、已完成、已作废）
    public enum OrderStatus {
        /**
         * 未处理
         */
        unprocessed("未处理"),
        /**
         * 已处理
         */
        processed("已处理"),
        /**
         * 已完成
         */
        completed("已完成"),
        /**
         * 已作废
         */
        invalid("已作废");

        private String value;

        private OrderStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }


    }

    // 付款状态（未支付、部分支付、已支付、部分退款、全额退款）
    public enum PaymentStatus {
        unpaid, partPayment, paid, partRefund, refunded
    }

    // 配送状态（未发货、部分发货、已发货、部分退货、已退货）
    public enum ShippingStatus {
        unshipped, partShipped, shipped, partReshiped, reshiped
    }

    @Id
    @Column(name = "ID", insertable = true, updatable = false)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;

    @Column(name = "SN", nullable = false, unique = true)
    @GenericGenerator(name = "serialnumber", strategy = "serialnumber", parameters = {@Parameter(name = "expression", value = "'SN_' + #DateUtil.format('yyyyMMdd') + #StringUtil.addZeroLeft(#SequenceInfo.nextValue('ORDER-SN'), 5)")})
    private String sn;// 订单编号
    @Column(name = "ORDER_TYPE", length = 20, updatable = false, nullable = false)
    private String orderType = "normal";//订单类型,预留字段。
    @Enumerated(EnumType.STRING)
    @Column(name = "ORDER_STATUS", length = 20, nullable = false)
    private OrderStatus orderStatus;// 订单状态
    @Enumerated(EnumType.STRING)
    @Column(name = "PAYMENT_STATUS", length = 20, nullable = false)
    private PaymentStatus paymentStatus;// 支付状态
    @Enumerated(EnumType.STRING)
    @Column(name = "SHIPPING_STATUS", length = 20, nullable = false)
    private ShippingStatus shippingStatus;// 发货状态
    @Column(name = "TOTAL_PRODUCT_PRICE", nullable = false, precision = 15, scale = 5)
    private BigDecimal totalProductPrice;// 总商品价格

    @Column(name = "TOTAL_AMOUNT", nullable = false, precision = 15, scale = 5)
    private BigDecimal totalAmount;// 订单总额
    @Column(name = "PAID_AMOUNT", nullable = false, precision = 15, scale = 5)
    private BigDecimal paidAmount;// 已付金额
    @Column(name = "TOTAL_PRODUCT_WEIGHT", nullable = false)
    private Integer totalProductWeight;// 总商品重量(单位: 克)
    @Column(name = "TOTAL_PRODUCT_QUANTITY", nullable = false)
    private Integer totalProductQuantity;// 总商品数量

    @Column(name = "SHIP_NAME", nullable = false)
    private String shipName;// 收货人姓名
    @Column(name = "SHIP_AREA_STORE", nullable = false)
    private String shipAreaStore;// 收货地区存储
    @Column(name = "SHIP_ADDRESS", nullable = false)
    private String shipAddress;// 收货地址
    @Column(name = "SHIP_ZIP_CODE", nullable = false)
    private String shipZipCode;// 收货邮编
    @Column(name = "SHIP_PHONE", nullable = false)
    private String shipPhone;// 收货电话
    @Column(name = "SHIP_MOBILE", nullable = false)
    private String shipMobile;// 收货手机
    @Column(name = "MEMO", nullable = false)
    private String memo;// 买家附言

    @Column(name = "DELIVERY_TYPE_NAME", nullable = true, length = 100)
    private String deliveryTypeName;// 配送方式名称
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DELIVERY_TYPE_ID",foreignKey =  @ForeignKey(name = "FK_ORDER_DELIVERY_TYPE"))
    private DeliveryType deliveryType;// 配送方式
    @Column(name = "DELIVERY_FEE", nullable = false, precision = 15, scale = 5)
    private BigDecimal deliveryFee;// 配送费用
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    @OrderBy("createTime asc")
    private List<Shipping> shippings;// 配送信息

    @Column(name = "PAYMENT_CONFIG_NAME", nullable = false)
    private String paymentConfigName;// 支付方式名称
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAYMENT_CONFIG_ID",foreignKey =  @ForeignKey(name = "FK_ORDER_PAYMENT_CONFIG"))

    private PaymentConfig paymentConfig;// 支付方式
    @Column(name = "PAYMENT_FEE", nullable = false, precision = 15, scale = 5)
    private BigDecimal paymentFee;// 支付手续费

    @Column(name = "goodsIdListStore", length = 3000)
    private String goodsIdListStore;// 商品ID集合储存

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID",foreignKey =  @ForeignKey(name = "FK_ORDER_MEMBER"))

    private Member member;// 会员

    @Element(OrderItem.class)
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    @OrderBy("createTime asc")
    private List<OrderItem> orderItems = new ArrayList<OrderItem>();// 订单支付信息

    /**
     * 数据版本
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VERSION_ID", foreignKey = @ForeignKey(name = "FK_MALL_GOODS_VERSION"))
    private AttributeVersion version;
    /**
     * 动态属性集合。
     */
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumns(value = {@JoinColumn(name = "TARGET_ID", referencedColumnName = "ID"), @JoinColumn(name = "VERSION_ID", referencedColumnName = "VERSION_ID")})
    private List<AttributeValue> attributeValues;

    /**
     * 临时字段，用于订单提交时，保存用户收货地址的id
     */
    @Transient
    @JsonIgnore
    private Long receiverId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public ShippingStatus getShippingStatus() {
        return shippingStatus;
    }

    public void setShippingStatus(ShippingStatus shippingStatus) {
        this.shippingStatus = shippingStatus;
    }

    public BigDecimal getTotalProductPrice() {
        return totalProductPrice;
    }

    public void setTotalProductPrice(BigDecimal totalProductPrice) {
        this.totalProductPrice = totalProductPrice;
    }

    public BigDecimal getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(BigDecimal deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public BigDecimal getPaymentFee() {
        return paymentFee;
    }

    public void setPaymentFee(BigDecimal paymentFee) {
        this.paymentFee = paymentFee;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public Integer getTotalProductWeight() {
        return totalProductWeight;
    }

    public void setTotalProductWeight(Integer totalProductWeight) {
        this.totalProductWeight = totalProductWeight;
    }

    public Integer getTotalProductQuantity() {
        return totalProductQuantity;
    }

    public void setTotalProductQuantity(Integer totalProductQuantity) {
        this.totalProductQuantity = totalProductQuantity;
    }

    public String getGoodsIdListStore() {
        return goodsIdListStore;
    }

    public void setGoodsIdListStore(String goodsIdListStore) {
        this.goodsIdListStore = goodsIdListStore;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    @SuppressWarnings("unchecked")
    public List<OrderItem> getOrderItems() {
        return (List<OrderItem>) ObjectUtil.defaultValue(orderItems, Collections.emptyList());
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public String getDeliveryTypeName() {
        return deliveryTypeName;
    }

    public void setDeliveryTypeName(String deliveryTypeName) {
        this.deliveryTypeName = deliveryTypeName;
    }

    public List<Shipping> getShippings() {
        return shippings;
    }

    public void setShippings(List<Shipping> shippings) {
        this.shippings = shippings;
    }

    public DeliveryType getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(DeliveryType deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public String getShipAreaStore() {
        return shipAreaStore;
    }

    @TypeConversion(key = "shipAreaStore", converter = "com.fantasy.common.bean.converter.AreaStoreConverter")
    public void setShipAreaStore(String shipAreaStore) {
        this.shipAreaStore = shipAreaStore;
    }

    public String getShipAddress() {
        return shipAddress;
    }

    public void setShipAddress(String shipAddress) {
        this.shipAddress = shipAddress;
    }

    public String getShipZipCode() {
        return shipZipCode;
    }

    public void setShipZipCode(String shipZipCode) {
        this.shipZipCode = shipZipCode;
    }

    public String getShipPhone() {
        return shipPhone;
    }

    public void setShipPhone(String shipPhone) {
        this.shipPhone = shipPhone;
    }

    public String getShipMobile() {
        return shipMobile;
    }

    public void setShipMobile(String shipMobile) {
        this.shipMobile = shipMobile;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getPaymentConfigName() {
        return paymentConfigName;
    }

    public void setPaymentConfigName(String paymentConfigName) {
        this.paymentConfigName = paymentConfigName;
    }

    public PaymentConfig getPaymentConfig() {
        return paymentConfig;
    }

    public void setPaymentConfig(PaymentConfig paymentConfig) {
        this.paymentConfig = paymentConfig;
    }

    @Override
    public AttributeVersion getVersion() {
        return version;
    }

    @Override
    public void setVersion(AttributeVersion version) {
        this.version = version;
    }

    @Override
    public List<AttributeValue> getAttributeValues() {
        return attributeValues;
    }

    @Override
    public void setAttributeValues(List<AttributeValue> attributeValues) {
        this.attributeValues = attributeValues;
    }

    @Transient
    public Area getShipArea() {
        if (StringUtil.isBlank(this.shipAreaStore)) {
            return null;
        }
        return JSON.deserialize(this.shipAreaStore, Area.class);
    }
}