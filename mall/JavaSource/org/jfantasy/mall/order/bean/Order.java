package org.jfantasy.mall.order.bean;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Persister;
import org.jfantasy.attr.framework.query.DynaBeanEntityPersister;
import org.jfantasy.attr.storage.BaseDynaBean;
import org.jfantasy.common.bean.Area;
import org.jfantasy.common.bean.converter.AreaConverter;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.member.bean.Member;

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
@Persister(impl = DynaBeanEntityPersister.class)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonFilter(JSON.CUSTOM_FILTER)
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler", "shipAreaStore", "memeo", "shippings", "orderItems", "payments"})
public class Order extends BaseDynaBean {

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
    @Convert(converter = AreaConverter.class)
    private Area shipArea;// 收货地区存储
    @Column(name = "SHIP_ADDRESS", nullable = false)
    private String shipAddress;// 收货地址
    @Column(name = "SHIP_ZIP_CODE", nullable = false)
    private String shipZipCode;// 收货邮编
    @Column(name = "SHIP_MOBILE", nullable = false)
    private String shipMobile;// 收货手机
    @Column(name = "MEMO", nullable = false)
    private String memo;// 买家附言

    @Column(name = "DELIVERY_TYPE_NAME", nullable = true, length = 100)
    private String deliveryTypeName;// 配送方式名称
    @Column(name = "DELIVERY_TYPE_ID")
    private Long deliveryTypeId;// 配送方式
    @Column(name = "DELIVERY_FEE", nullable = false, precision = 15, scale = 5)
    private BigDecimal deliveryFee;// 配送费用
    @Column(name = "PAYMENT_CONFIG_NAME", nullable = false)
    private String paymentConfigName;// 支付方式名称

    /*
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAYMENT_CONFIG_ID", foreignKey = @ForeignKey(name = "FK_ORDER_PAYMENT_CONFIG"))
    private PaymentConfig paymentConfig;// 支付方式
    */

    @Column(name = "PAYMENT_FEE", nullable = false, precision = 15, scale = 5)
    private BigDecimal paymentFee;// 支付手续费

    @Column(name = "goodsIdListStore", length = 3000)
    private String goodsIdListStore;// 商品ID集合储存

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", foreignKey = @ForeignKey(name = "FK_ORDER_MEMBER"))
    private Member member;// 会员

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    @OrderBy("createTime asc")
    private List<OrderItem> orderItems = new ArrayList<OrderItem>();// 订单支付信息

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

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public Area getShipArea() {
        return shipArea;
    }

    public void setShipArea(Area shipArea) {
        this.shipArea = shipArea;
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

    public Long getDeliveryTypeId() {
        return deliveryTypeId;
    }

    public void setDeliveryTypeId(Long deliveryTypeId) {
        this.deliveryTypeId = deliveryTypeId;
    }

    public void setPaymentConfigName(String paymentConfigName) {
        this.paymentConfigName = paymentConfigName;
    }

    /*
    public PaymentConfig getPaymentConfig() {
        return paymentConfig;
    }

    public void setPaymentConfig(PaymentConfig paymentConfig) {
        this.paymentConfig = paymentConfig;
    }
    */

}