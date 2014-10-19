package com.fantasy.cms.ws.dto;

import java.io.Serializable;

public class ArticleCategoryDTO implements Serializable {

	private static final long serialVersionUID = -7217345631547439291L;
	/**
	 * id
	 */
	private String code;
	/**
	 * 栏目名称
	 */
	private String name;

	/**
	 * 层级
	 */
	private Integer layer;
	/**
	 * 路径
	 */
	private String path;// 树路径
	/**
	 * 描述
	 */

	private String description;
	/**
	 * 排序字段
	 * 
	 */
	private Integer sort;
	/**
	 * 上级栏目
	 * 
	 */
	private ArticleCategoryDTO parent;

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

	public Integer getLayer() {
		return layer;
	}

	public void setLayer(Integer layer) {
		this.layer = layer;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
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

	public ArticleCategoryDTO getParent() {
		return parent;
	}

	public void setParent(ArticleCategoryDTO parent) {
		this.parent = parent;
	}

}
