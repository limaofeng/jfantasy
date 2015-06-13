package com.fantasy.mall.delivery.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import com.fantasy.mall.order.bean.Order;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 送货信息表
 * 
 * @author 李茂峰
 * @version 1.0
 * @since 2013-10-15 下午3:37:40
 */
@Entity
@Table(name = "MALL_DELIVERY_SHIPPING")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "order", "deliveryItems", "shipAreaStore" })
public class Shipping extends BaseBusEntity {

	private static final long serialVersionUID = 4315245804828793329L;
	@Id
	@Column(name = "ID", insertable = true, updatable = false)
	@GeneratedValue(generator = "fantasy-sequence")
	@GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
	private Long id;
	@Column(name = "SN", nullable = false, unique = true)
	@GenericGenerator(name = "serialnumber", strategy = "serialnumber", parameters = { @Parameter(name = "expression", value = "'SN_' + #DateUtil.format('yyyyMMdd') + #StringUtil.addZeroLeft(#SequenceInfo.nextValue('SHIPPING-SN'), 5)") })
	private String sn;// 发货编号
	@Column(name = "DELIVERY_TYPE_NAME", length = 50)
	private String deliveryTypeName;// 配送方式名称
	@Column(name = "DELIVERY_CORP_NAME", length = 50)
	private String deliveryCorpName; // 物流公司名称
	@Column(name = "DELIVERY_CORP_URL", length = 50)
	private String deliveryCorpUrl;// 物流公司网址
	@Column(name = "DELIVERY_SN", length = 50)
	private String deliverySn;// 物流单号
	@Column(name = "DELIVERY_FEE", precision = 10, scale = 2)
	private BigDecimal deliveryFee;// 物流费用
	@Column(name = "SHIP_NAME", length = 50)
	private String shipName;// 收货人姓名
	@Column(name = "SHIP_AREA_STORE", length = 300)
	private String shipAreaStore;// 收货地区存储
	@Column(name = "SHIP_ADDRESS", length = 150)
	private String shipAddress;// 收货地址
	@Column(name = "SHIP_ZIP_CODE", length = 10)
	private String shipZipCode;// 收货邮编
	@Column(name = "SHIP_PHONE", length = 12)
	private String shipPhone;// 收货电话
	@Column(name = "SHIP_MOBILE", length = 12)
	private String shipMobile;// 收货手机
	@Column(name = "memo", length = 150)
	private String memo;// 备注

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "ORDER_ID",foreignKey = @ForeignKey(name = "FK_SHIPPING_ORDER"))

	private Order order;// 订单

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "DELIVERY_TYPE_ID",foreignKey = @ForeignKey(name = "FK_SHIPPING_DELIVERY_TYPE"))

	private DeliveryType deliveryType;// 配送方式

	@OneToMany(mappedBy = "shipping", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<DeliveryItem> deliveryItems = new ArrayList<DeliveryItem>(); // 物流项

	public String getDeliveryTypeName() {
		return deliveryTypeName;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
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

	public String getDeliveryCorpUrl() {
		return deliveryCorpUrl;
	}

	public void setDeliveryCorpUrl(String deliveryCorpUrl) {
		this.deliveryCorpUrl = deliveryCorpUrl;
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

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public DeliveryType getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(DeliveryType deliveryType) {
		this.deliveryType = deliveryType;
	}

	public List<DeliveryItem> getDeliveryItems() {
		return deliveryItems;
	}

	public void setDeliveryItems(List<DeliveryItem> deliveryItems) {
		this.deliveryItems = deliveryItems;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}