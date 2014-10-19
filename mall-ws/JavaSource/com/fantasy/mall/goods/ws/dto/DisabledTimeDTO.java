package com.fantasy.mall.goods.ws.dto;

import java.io.Serializable;

public class DisabledTimeDTO implements Serializable{


	private static final long serialVersionUID = -4113450228729589714L;
	
	
	private String id;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 开始时间
	 */
	private String starttime;
	/**
	 * 备注
	 */
	private String remark;
	
	private Integer sort;
	
	
	private String time;
	

	
	/**
	 * 结束时间
	 */
	private String endtime;
	

	
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	
	

}
