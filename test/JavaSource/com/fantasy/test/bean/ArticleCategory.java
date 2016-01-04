package org.jfantasy.test.bean;

import org.jfantasy.attr.storage.bean.AttributeVersion;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.lucene.annotations.IndexEmbedBy;
import org.jfantasy.framework.lucene.annotations.IndexProperty;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "TEST_ARTICLE_CATEGORY")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "articles", "articleVersion"})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ArticleCategory extends BaseBusEntity {

    public static final String PATH_SEPARATOR = ",";// 树路径分隔符

    @Id
    @Column(name = "CODE", length = 50, insertable = true, updatable = false)
    private String code;
    /**
     * 栏目名称
     */
    @Column(name = "NAME", length = 200)
    @IndexProperty(analyze = true)
    private String name;
    /**
     * 层级
     */
    @Column(name = "LAYER", nullable = false)
    private Integer layer;
    // 树路径
    @IndexEmbedBy(value = Article.class)
    @Column(name = "PATH", nullable = false, length = 3000)
    private String path;
    /**
     * 描述
     */
    @Column(name = "DESCRIPTION", length = 2000)
    private String description;
    /**
     * 排序字段
     */
    @Column(name = "SORT")
    private Integer sort;
    /**
     * 上级栏目
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PCODE", foreignKey = @ForeignKey(name = "FK_TEST_CATEGORY_PARENT"))
    @JsonManagedReference
    private ArticleCategory parent;
    /**
     * 下级栏目
     */
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    @OrderBy("sort ASC")
    @JsonBackReference
    private List<ArticleCategory> children;
    /**
     * 属性版本表
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ARTICLE_VERSION_ID", foreignKey = @ForeignKey(name = "FK_TEST_ARTICLE_CATEGORY_VERSION"))
    private AttributeVersion articleVersion;
    /**
     * 文章
     */
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
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

    public AttributeVersion getArticleVersion() {
        return articleVersion;
    }

    public void setArticleVersion(AttributeVersion articleVersion) {
        this.articleVersion = articleVersion;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
