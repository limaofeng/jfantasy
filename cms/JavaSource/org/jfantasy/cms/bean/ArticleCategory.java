package org.jfantasy.cms.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jfantasy.cms.bean.databind.ArticleCategoryDeserializer;
import org.jfantasy.cms.bean.databind.ArticleCategorySerializer;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.lucene.annotations.IndexEmbedBy;
import org.jfantasy.framework.lucene.annotations.IndexProperty;

import javax.persistence.*;
import java.util.List;

/**
 * 栏目表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2012-11-4 下午05:46:57
 */
@ApiModel("文章分类")
@Entity
@Table(name = "CMS_ARTICLE_CATEGORY")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler", "articles", "children"})
public class ArticleCategory extends BaseBusEntity {

    private static final long serialVersionUID = -2207100604803274789L;

    public static final String PATH_SEPARATOR = ",";// 树路径分隔符

    @Id
    @Column(name = "CODE", length = 50, insertable = true, updatable = false)
    private String code;
    /**
     * 栏目名称
     */
    @ApiModelProperty("名称")
    @Column(name = "NAME", length = 200)
    @IndexProperty(analyze = true)
    private String name;
    /**
     * 层级
     */
    @ApiModelProperty("层级")
    @Column(name = "LAYER", nullable = false)
    private Integer layer;
    // 树路径
    @ApiModelProperty(value = "路径", notes = "该字段不需要手动维护")
    @IndexEmbedBy(value = Article.class)
    @Column(name = "PATH", nullable = false, length = 3000)
    private String path;
    /**
     * 描述
     */
    @ApiModelProperty("描述")
    @Column(name = "DESCRIPTION", length = 2000)
    private String description;
    /**
     * 排序字段
     */
    @ApiModelProperty("排序字段")
    @Column(name = "SORT")
    private Integer sort;
    /**
     * 上级栏目
     */
    @ApiModelProperty("上级分类")
    @JsonProperty("parentCode")
    @JsonSerialize(using = ArticleCategorySerializer.class)
    @JsonDeserialize(using = ArticleCategoryDeserializer.class)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PCODE", foreignKey = @ForeignKey(name = "FK_CMS_CATEGORY_PARENT"))
    private ArticleCategory parent;
    /**
     * 下级栏目
     */
    @ApiModelProperty(hidden = true)
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    @OrderBy("sort ASC")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private List<ArticleCategory> children;
    /**
     * 文章
     */
    @ApiModelProperty(hidden = true)
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

    public Integer getLayer() {
        return layer;
    }

    public void setLayer(Integer layer) {
        this.layer = layer;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
