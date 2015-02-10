package com.fantasy.cms.ws.dto;

import java.io.Serializable;

public class ArticleDTO implements Serializable {

	private static final long serialVersionUID = -3450531192706976441L;

	/**
	 * 文章id
	 */
	private Long id;

	/**
	 * 标题
	 */
	private String title;
	/**
	 * 摘要
	 */
	private String summary;
	/**
	 * 关键词
	 */
	private String keywords;
	/**
	 * 文章正文
	 */
	private String content;

	/**
	 * 作者
	 */
	private String author;
	/**
	 * 发布日期
	 */
	private String releaseDate;
	/**
	 * 文章对应的栏目
	 * 
	 */
	private ArticleCategoryDTO category;

    /**
     * 动态属性
     */
    private AttributeValueDTO[] attributeValues;

    /**
     * 图片路径
     */
	@Deprecated
    private String articleImageStore;

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public ArticleCategoryDTO getCategory() {
		return category;
	}

	public void setCategory(ArticleCategoryDTO category) {
		this.category = category;
	}

    public AttributeValueDTO[] getAttributeValues() {
        return this.attributeValues;
    }

    public void setAttributeValues(AttributeValueDTO[] attributeValues) {
        this.attributeValues = attributeValues;
    }

    public String getArticleImageStore() {
        return articleImageStore;
    }

    public void setArticleImageStore(String articleImageStore) {
        this.articleImageStore = articleImageStore;
    }
}
