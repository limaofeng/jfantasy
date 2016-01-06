package org.jfantasy.mall.stock.bean;

import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.util.jackson.JSON;
import org.jfantasy.mall.goods.bean.Product;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 *@Author lsz
 *@Date 2013-11-28 下午2:48:12
 *
 */
@Entity
@Table(name = "MALL_STOCK")
@JsonFilter(JSON.CUSTOM_FILTER)
@JsonIgnoreProperties({ "hibernateLazyInitializer"})
public class Stock extends BaseBusEntity{

	private static final long serialVersionUID = 3220895257066557468L;
	
	@Id
	@Column(name = "ID", insertable = true, updatable = false)
	@GeneratedValue(generator = "fantasy-sequence")
	@GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
	private Long id;
	/**
	 * 添加量
	 */
	@Column(name="CHANGE_NUMBER",nullable = false)
	private Integer changeNumber;
	/**
	 * 备注
	 */
	@Column(name="REMARK",nullable=false)
	private String remark;
	
	/**
	 * 状态 true:加，false:减
	 */
	@Column(name="STATUS",nullable=false)
	private Boolean status;
	
	/**
	 * 商品
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRODUCGT_ID", nullable = false,foreignKey =@ForeignKey(name = "FK_PRODUCT_STOCE") )

	private Product product;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getChangeNumber() {
		return changeNumber;
	}

	public void setChangeNumber(Integer changeNumber) {
		this.changeNumber = changeNumber;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}
}

