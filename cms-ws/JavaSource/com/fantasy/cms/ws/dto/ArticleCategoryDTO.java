package com.fantasy.cms.ws.dto;

import java.io.Serializable;

public class ArticleCategoryDTO implements Serializable {

	private static final long serialVersionUID = -7217345631547439291L;

	/**
	 * 栏目编码
	 */
	private String code;
	/**
	 * 栏目名称
	 */
	private String name;

    /**
     * 栏目英文名称
     */
    private String engname;

	/**
	 * 层级
	 */
	private Integer layer;
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
     * 文章数组
     */
    private ArticleDTO[] articleDTOs;

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


    public ArticleDTO[] getArticleDTOs() {
        return articleDTOs;
    }

    public void setArticleDTOs(ArticleDTO[] articleDTOs) {
        this.articleDTOs = articleDTOs;
    }

    public String getEngname() {
        return engname;
    }

    public void setEngname(String engname) {
        this.engname = engname;
    }
}
