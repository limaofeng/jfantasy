package com.fantasy.mall.order.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.mall.goods.bean.Goods;
import com.fantasy.mall.goods.bean.Product;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 订单明细表
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-9-22 下午1:59:27
 * @version 1.0
 */
@Entity
@Table(name = "MALL_ORDER_ITEM")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonFilter(JSON.CUSTOM_FILTER)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "memeo", "order" })
public class OrderItem extends BaseBusEntity {

	private static final long serialVersionUID = 5030818078599298690L;

	/**
	 * 初始化订单项
	 * 
	 * @param product
	 * @return
	 */
	public void initialize(Product product) {
		this.setSn(product.getSn());
		this.setName(product.getName());
		this.setProductPrice(product.getPrice());
		this.setDeliveryQuantity(0);
        Product p1 = JSON.deserialize(JSON.serialize(product), Product.class);
        p1.setGoods(JSON.deserialize(JSON.serialize(product.getGoods()), Goods.class));
        this.setProduct(p1);
		this.setAttr("");
	}

	@Id
	@GeneratedValue(generator = "fantasy-sequence")
	@GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
	@Column(name = "ID", insertable = true, updatable = false)
	private Long id;
	@Column(name = "SN", nullable = false, updatable = false)
	private String sn;// 商品货号
	@Column(name = "NAME", nullable = false, updatable = false)
	private String name;// 商品名称
	/**
	 * 商品价格
	 */
	@Column(name = "PRODUCT_PRICE", nullable = false, precision = 15, scale = 5)
	private BigDecimal productPrice;
	/**
	 * 商品数量
	 */
	@Column(name = "PRODUCT_QUANTITY", nullable = false)
	private Integer productQuantity;
	@Column(name = "DELIVERY_QUANTITY", nullable = false)
	private Integer deliveryQuantity;// 发货数量
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORDER_ID", nullable = false,foreignKey =@ForeignKey(name = "FK_ORDER_ITEM_ORDER") )

	private Order order;// 订单
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRODUCT_ID",foreignKey = @ForeignKey(name = "FK_ORDER_ITEM_PRODUCT"))

	private Product product;// 商品
	@Column(name = "attr", length = 4000)
	private String attr; // 商品属性

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(BigDecimal productPrice) {
		this.productPrice = productPrice;
	}

	public Integer getProductQuantity() {
		return productQuantity;
	}

	public void setProductQuantity(Integer productQuantity) {
		this.productQuantity = productQuantity;
	}

	public Integer getDeliveryQuantity() {
		return deliveryQuantity;
	}

	public void setDeliveryQuantity(Integer deliveryQuantity) {
		this.deliveryQuantity = deliveryQuantity;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getAttr() {
		return attr;
	}

	public void setAttr(String attr) {
		this.attr = attr;
	}

	/**
	 * 重量小计
	 * 
	 * @return
	 */
	@Transient
	public Integer getSubtotalWeight() {
		return this.getProduct().getWeight() * this.getProductQuantity();
	}

	/**
	 * 价格小计
	 * 
	 * @return
	 */
	@Transient
	public BigDecimal getSubtotalPrice() {
		return productPrice.multiply(new BigDecimal(productQuantity));
	}
}