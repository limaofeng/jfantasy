package com.fantasy.mall.goods.ws.dto;

public class GoodsParameterValueDTO implements Comparable<GoodsParameterValueDTO> {
	private String id;
	private String name;
	private String value;
	private Integer sort;


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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int compareTo(GoodsParameterValueDTO parameterValue) {
		if (parameterValue.getSort() == null) {
			return 1;
		}
		if (this.getSort() == null) {
			return -1;
		}
		return this.getSort().compareTo(parameterValue.getSort());
	}
}
