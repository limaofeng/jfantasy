package com.fantasy.mall.goods.bean;

import java.util.UUID;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class SpecificationValue implements Comparable<SpecificationValue> {
	private String id;
	private String value;
	private Integer sort;

	public SpecificationValue() {
		this.id = UUID.randomUUID().toString();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof SpecificationValue) {
			SpecificationValue specificationValue = (SpecificationValue) o;
			return new EqualsBuilder().appendSuper(super.equals(o)).append(this.getId(), specificationValue.getId()).append(this.getValue(), specificationValue.getValue()).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().appendSuper(super.hashCode()).append(this.getId()).append(this.getValue()).toHashCode();
	}

	public int compareTo(SpecificationValue specificationValue) {
		if (specificationValue.getSort() == null) {
			return 1;
		}
		if (this.getSort() == null) {
			return -1;
		}
		return this.getSort().compareTo(specificationValue.getSort());
	}
}
