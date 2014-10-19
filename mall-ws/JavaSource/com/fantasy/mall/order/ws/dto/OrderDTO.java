package com.fantasy.mall.order.ws.dto;


import com.fantasy.member.ws.dto.MemberDTO;

import java.math.BigDecimal;

public class OrderDTO {

    private Long id;

    private String sn;// 订单编号

    private String orderType ;//订单类型,预留字段。

    private String orderStatus;// 订单状态

    private String paymentStatus;// 支付状态

  //  private ShippingStatus shippingStatus;// 发货状态

    private BigDecimal totalProductPrice;// 总商品价格


    private BigDecimal totalAmount;// 订单总额

    private BigDecimal paidAmount;// 已付金额

    private Integer totalProductWeight;// 总商品重量(单位: 克)

    private Integer totalProductQuantity;// 总商品数量


    private DeliveryTypeDTO deliveryTypeDTO;// 配送方式

    private String shipName;// 收货人姓名

    private String shipAreaStore;// 收货地区存储

    private String shipAddress;// 收货地址

    private String shipZipCode;// 收货邮编

    private String shipPhone;// 收货电话

    private String shipMobile;// 收货手机

    private String memo;// 买家附言


    public DeliveryTypeDTO getDeliveryTypeDTO() {
        return deliveryTypeDTO;
    }

    public void setDeliveryTypeDTO(DeliveryTypeDTO deliveryTypeDTO) {
        this.deliveryTypeDTO = deliveryTypeDTO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public BigDecimal getTotalProductPrice() {
        return totalProductPrice;
    }

    public void setTotalProductPrice(BigDecimal totalProductPrice) {
        this.totalProductPrice = totalProductPrice;
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

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public String getShipAreaStore() {
        return shipAreaStore;
    }

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

    public String getPaymentConfigName() {
        return paymentConfigName;
    }

    public void setPaymentConfigName(String paymentConfigName) {
        this.paymentConfigName = paymentConfigName;
    }



    public BigDecimal getPaymentFee() {
        return paymentFee;
    }

    public void setPaymentFee(BigDecimal paymentFee) {
        this.paymentFee = paymentFee;
    }

    public String getGoodsIdListStore() {
        return goodsIdListStore;
    }

    public void setGoodsIdListStore(String goodsIdListStore) {
        this.goodsIdListStore = goodsIdListStore;
    }

    public MemberDTO getMember() {
        return member;
    }

    public void setMember(MemberDTO member) {
        this.member = member;
    }

    public OrderItemDTO[] getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(OrderItemDTO[] orderItems) {
        this.orderItems = orderItems;
    }
// private String deliveryTypeName;// 配送方式名称

    //private DeliveryType deliveryType;// 配送方式

  //private BigDecimal deliveryFee;// 配送费用

    //private List<Shipping> shippings;// 配送信息


    private String paymentConfigName;// 支付方式名称

  //  private PaymentConfig paymentConfig;// 支付方式

    private BigDecimal paymentFee;// 支付手续费


    private String goodsIdListStore;// 商品ID集合储存


    private MemberDTO member;// 会员

    private OrderItemDTO[] orderItems;// 订单支付信息


}
