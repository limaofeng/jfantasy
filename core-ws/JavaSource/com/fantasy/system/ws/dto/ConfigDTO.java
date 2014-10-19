package com.fantasy.system.ws.dto;

import java.util.List;

/**
 * 系统参数配置表
 * 
 * @author
 */
public class ConfigDTO {


	/**
	 * 代码
	 */
	private String code;
	/**
	 * 配置类别
	 */
	private String type;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 排序字段
	 */
	private Integer sort;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 上级编码
	 */
	private ConfigDTO parent;

	private List<ConfigDTO> children;

	public List<ConfigDTO> getChildren() {
		return children;
	}

	public void setChildren(List<ConfigDTO> children) {
		this.children = children;
	}

	/**
	 * 设置 代码
	 * 
	 * @param code
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 获取 代码
	 * 
	 * @return java.lang.String
	 */
	public String getCode() {
		return this.code;
	}

	/**
	 * 设置 名称
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取 名称
	 * 
	 * @return java.lang.String
	 */
	public String getName() {
		return this.name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ConfigDTO getParent() {
		return parent;
	}
	public ConfigKeyDTO getConfigKey() {
		return ConfigKeyDTO.newInstance(this.code, this.type);
	}

	public ConfigKeyDTO getParentKey() {
		if (this.parent == null) {
			return null;
		}
		return ConfigKeyDTO.newInstance(this.parent.code, this.parent.type);
	}

	public void setParent(ConfigDTO parent) {
		this.parent = parent;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getParentName() {
		if (this.getParent() == null)
			return "";
		String parentName = this.getParent().getParentName();
		return parentName + (parentName.equals("") ? "" : ">") + this.getParent().getName();
	}

}