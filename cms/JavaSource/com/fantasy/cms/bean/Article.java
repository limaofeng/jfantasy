package com.fantasy.cms.bean;

import com.fantasy.attr.framework.query.DynaBeanEntityPersister;
import com.fantasy.attr.storage.BaseDynaBean;
import com.fantasy.cms.bean.databind.ContentDeserializer;
import com.fantasy.framework.lucene.annotations.*;
import com.fantasy.framework.util.jackson.JSON;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Persister;

import javax.persistence.*;
import javax.persistence.Entity;

/**
 * 文章表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2012-11-4 下午05:47:20
 */
@ApiModel("文章")
@Indexed
@Entity
@Table(name = "CMS_ARTICLE")
@Persister(impl = DynaBeanEntityPersister.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonFilter(JSON.CUSTOM_FILTER)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "keywords", "category", "content", "target"})
public class Article extends BaseDynaBean {

    private static final long serialVersionUID = 3480217915594201004L;

    enum Type {
        /**
         * 原创
         */
        original,//NOSONAR
        /**
         * 转载
         */
        transfer,//NOSONAR
        /**
         * 链接
         */
        link//NOSONAR
    }

    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = true, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 文章标题
     */
    @ApiModelProperty("文章标题")
    @IndexProperty(analyze = true, store = true)
    @Column(name = "TITLE")
    private String title;
    /**
     * 摘要
     */
    @ApiModelProperty("摘要")
    @IndexProperty(analyze = true, store = true)
    @Column(name = "SUMMARY", length = 500)
    private String summary;
    /**
     * 关键词
     */
    @ApiModelProperty("关键词")
    @Column(name = "KEYWORDS")
    private String keywords;
    /**
     * 文章正文
     */
    @ApiModelProperty(hidden = true)
    @IndexProperty(analyze = true, store = true)
    @JoinColumn(name = "CONTENT_ID")
    @JsonDeserialize(using = ContentDeserializer.class)
    @OneToOne(targetEntity = Content.class, fetch = FetchType.LAZY, cascade = {CascadeType.ALL, CascadeType.REMOVE})
    private Content content;
    /**
     * 作者
     */
    @ApiModelProperty("作者")
    @Column(name = "AUTHOR")
    private String author;
    /**
     * 发布日期
     */
    @ApiModelProperty("发布日期")
    @IndexProperty(store = true)
    @Column(name = "RELEASE_DATE")
    private String releaseDate;
    /**
     * 文章对应的栏目
     */
    @ApiModelProperty("对应的分类")
    @IndexEmbed
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_CODE", nullable = false, foreignKey = @ForeignKey(name = "FK_CMS_ARTICLE_CATEGORY"))
    private ArticleCategory category;
    /**
     * 发布标志
     */
    @ApiModelProperty("发布标志")
    @IndexFilter(compare = Compare.IS_EQUALS, value = "true")
    @Column(name = "ISSUE")
    private Boolean issue;

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

    public ArticleCategory getCategory() {
        return category;
    }

    public void setCategory(ArticleCategory category) {
        this.category = category;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Boolean getIssue() {
        return issue;
    }

    public void setIssue(Boolean issue) {
        this.issue = issue;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", keywords='" + keywords + '\'' +
                ", author='" + author + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", issue=" + issue +
                '}';
    }
}
