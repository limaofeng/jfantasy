package com.fantasy.mall.goods.bean;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.fantasy.framework.util.common.StringUtil;

/**
 * 商品参数
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2014-4-5 上午8:15:50
 * @version 1.0
 */
public class GoodsParameter implements Comparable<GoodsParameter> {
	/**
	 * 索引Id
	 */
	private String id;// ID
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 格式
	 */
	private String format;
	/**
	 * 排序
	 */
	private Integer sort;
	/**
	 * 备注
	 */
	private String remark;

	public GoodsParameter() {
		this.id = StringUtil.uuid();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
        if(StringUtil.isNotBlank(id)) {
            this.id = id;
        }
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof GoodsParameter) {
			GoodsParameter parameter = (GoodsParameter) o;
			return new EqualsBuilder().appendSuper(super.equals(o)).append(this.getId(), parameter.getId()).append(this.getName(), parameter.getName()).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().appendSuper(super.hashCode()).append(this.getId()).append(this.getName()).toHashCode();
	}

	public int compareTo(GoodsParameter parameter) {
		if (parameter.getSort() == null) {
			return 1;
		}
		if (this.getSort() == null) {
			return -1;
		}
		return this.getSort().compareTo(parameter.getSort());
	}

}