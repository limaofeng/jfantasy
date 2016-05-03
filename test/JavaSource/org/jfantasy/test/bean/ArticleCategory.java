package org.jfantasy.test.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties
public class ArticleCategory {

    private String code;
    /**
     * 栏目名称
     */
    private String name;
    /**
     * 层级
     */
    private Integer layer;
    // 树路径
    private String path;
    /**
     * 描述
     */
    private String description;
    /**
     * 排序字段
     */
    private Integer sort;
    /**
     * 上级栏目
     */
    private ArticleCategory parent;
    /**
     * 下级栏目
     */
    private List<ArticleCategory> children;
    /**
     * 文章
     */
    private List<Article> articles;

    public ArticleCategory() {
    }

    public ArticleCategory(String code) {
        this.setCode(code);
    }

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

    public ArticleCategory getParent() {
        return parent;
    }

    public void setParent(ArticleCategory parent) {
        this.parent = parent;
    }

    public List<ArticleCategory> getChildren() {
        return children;
    }

    public void setChildren(List<ArticleCategory> children) {
        this.children = children;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
