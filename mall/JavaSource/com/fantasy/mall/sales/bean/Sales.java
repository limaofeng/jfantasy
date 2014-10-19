package com.fantasy.mall.sales.bean;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import com.fantasy.common.bean.enums.TimeUnit;
import com.fantasy.framework.dao.BaseBusEntity;
import com.fantasy.framework.util.common.DateUtil;
import com.fantasy.framework.util.common.StringUtil;

/**
 * 销量数据表
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-12-9 上午11:31:29
 * @version 1.0
 */
@Entity
@Table(name = "MALL_SALES", uniqueConstraints = { @UniqueConstraint(columnNames = { "TYPE", "SN", "TIME_UNIT", "TIME" }) })
@JsonIgnoreProperties({ "hibernateLazyInitializer" })
public class Sales extends BaseBusEntity {
	private static final long serialVersionUID = 5134293616045211706L;

	public static enum Type {
		goods, product, order
	}

	@Id
	@Column(name = "ID", insertable = true, updatable = false)
	@GeneratedValue(generator = "fantasy-sequence")
	@GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
	private Long id;
	/**
	 * 类型
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "TYPE", length = 20)
	private Type type;

	/**
	 * 可能的值<br/>
	 * 时间 格式说明：<br/>
	 * TimeUnit.day = 20130103 <br/>
	 * TimeUnit.week = 201321 <br/>
	 * TimeUnit.month = 201312 <br/>
	 * TimeUnit.year = 2013
	 */
	@Column(name = "TIME", length = 8, nullable = true)
	private String time;
	/**
	 * 时间单位
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "TIME_UNIT", length = 8, nullable = true)
	private TimeUnit timeUnit;
	/**
	 * 对应货品、商品、订单 的sn
	 */
	@Column(name = "SN", length = 50, nullable = true)
	private String sn;
	/**
	 * 商品或者货品所属分类的path
	 */
	@Column(name = "PATH", length = 200, nullable = true)
	private String path;
	/**
	 * 统计数量
	 */
	@Column(name = "QUANTITY")
	private Integer quantity;
	/**
	 * 销售金额
	 */
	@Column(name = "AMOUNT")
	private BigDecimal amount;
	/**
	 * 商品退货量
	 */
	@Column(name = "RETURN_QUANTITY")
	private Integer returnQuantity;
	/**
	 * 退货金额
	 */
	@Column(name = "RETURN_AMOUNT")
	private Integer returnAmount;

	/**
	 * 统计开始时间
	 */
	@JsonIgnore
	@Transient
	private String startTime;
	/**
	 * 统计结束时间
	 */
	@JsonIgnore
	@Transient
	private String endTime;
	
	@Transient
	private Object target;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getQuantity() {
		return quantity == null ? 0 : quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public TimeUnit getTimeUnit() {
		return timeUnit;
	}

	public void setTimeUnit(TimeUnit timeUnit) {
		this.timeUnit = timeUnit;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public BigDecimal getAmount() {
		return amount == null ? BigDecimal.ZERO : amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Integer getReturnQuantity() {
		return returnQuantity;
	}

	public void setReturnQuantity(Integer returnQuantity) {
		this.returnQuantity = returnQuantity;
	}

	public Integer getReturnAmount() {
		return returnAmount;
	}

	public void setReturnAmount(Integer returnAmount) {
		this.returnAmount = returnAmount;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Integer getAvgQuantity() {
		if (StringUtil.isBlank(this.startTime) || StringUtil.isBlank(this.endTime) || Integer.valueOf(0).equals(this.getQuantity())) {
			return 0;
		}
		if (this.timeUnit == TimeUnit.day) {
			return (int) (this.getQuantity() / DateUtil.interval(DateUtil.parse(this.endTime, "yyyyMMdd"), DateUtil.parse(this.startTime, "yyyyMMdd"), Calendar.DATE));
		}
		return 0;
	}

	public BigDecimal getAvgAmount() {
		if (StringUtil.isBlank(this.startTime) || StringUtil.isBlank(this.endTime) || BigDecimal.ZERO.equals(this.getAmount())) {
			return BigDecimal.ZERO;
		}
		if (this.timeUnit == TimeUnit.day) {
			return this.getAmount().divide(BigDecimal.valueOf(DateUtil.interval(DateUtil.parse(this.endTime, "yyyyMMdd"), DateUtil.parse(this.startTime, "yyyyMMdd"), Calendar.DATE)), RoundingMode.HALF_DOWN);
		}
		return BigDecimal.ZERO;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

	public Object getTarget() {
		return this.target;
	}

}
