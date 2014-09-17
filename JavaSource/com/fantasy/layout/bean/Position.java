package com.fantasy.layout.bean;

import javax.persistence.Column;

/**
 *  推荐位表
 * @功能描述
 * @author 李茂峰
 * @since 2013-3-17 上午10:24:14
 * @version 1.0
 */
public class Position {

	/**
	 * 位置编码
	 */
	private String code;
	/**
	 * 位置名称
	 */
	private String name;
	/**
	 * 
	 */
	private String type;
	
	/**
	 * 位置描述
	 */
	@Column(name = "DESCRIPTION", length = 2000)
	private String description;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
