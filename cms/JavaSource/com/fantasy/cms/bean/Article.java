package com.fantasy.cms.bean;

import com.fantasy.attr.DynaBean;
import com.fantasy.attr.DynaBeanEntityPersister;
import com.fantasy.attr.bean.AttributeValue;
import com.fantasy.attr.bean.AttributeVersion;
import com.fantasy.framework.dao.BaseBusEntity;
import com.fantasy.framework.lucene.annotations.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Persister;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.List;

/**
 * 文章表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2012-11-4 下午05:47:20
 */
@Indexed
@Entity
@Table(name = "CMS_ARTICLE")
@Persister(impl = DynaBeanEntityPersister.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "content", "keywords", "version", "attributeValues"})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Article extends BaseBusEntity implements DynaBean {

    private static final long serialVersionUID = 3480217915594201004L;

    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = true, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 文章标题
     */
    @IndexProperty(analyze = true, store = true)
    @Column(name = "TITLE")
    private String title;
    /**
     * 摘要
     */
    @IndexProperty(analyze = true, store = true)
    @Column(name = "SUMMARY")
    private String summary;
    /**
     * 关键词
     */
    @Column(name = "KEYWORDS")
    private String keywords;
    /**
     * 文章正文
     */
    @IndexProperty(analyze = true, store = true)
    @JoinColumn(name = "CONTENT_ID")
    @OneToOne(targetEntity = Content.class, fetch = FetchType.LAZY, cascade = {CascadeType.ALL, CascadeType.REMOVE})
    private Content content;
    /**
     * 作者
     */
    @Column(name = "AUTHOR")
    private String author;
    /**
     * 发布日期
     */
    @IndexProperty(store = true)
    @Column(name = "RELEASE_DATE")
    private String releaseDate;
    /**
     * 文章对应的栏目
     */
    @IndexEmbed
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_CODE", nullable = false, foreignKey = @ForeignKey(name = "FK_CMS_ARTICLE_CATEGORY"))
    private ArticleCategory category;
    /**
     * 发布标志
     */
    @IndexFilter(compare = Compare.IS_EQUALS, value = "true")
    @Column(name = "ISSUE")
    private Boolean issue;
    /**
     * 数据版本
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VERSION_ID", foreignKey = @ForeignKey(name = "FK_CMS_ARTICLE_VERSION"))
    private AttributeVersion version;
    /**
     * 动态属性集合。 * 注意访问修饰符为 protected
     */
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumns(value = {@JoinColumn(name = "TARGET_ID", referencedColumnName = "ID"), @JoinColumn(name = "VERSION_ID", referencedColumnName = "VERSION_ID")})
    protected List<AttributeValue> attributeValues;

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

    public AttributeVersion getVersion() {
        return version;
    }

    public void setVersion(AttributeVersion version) {
        this.version = version;
    }

    public List<AttributeValue> getAttributeValues() {
        return attributeValues;
    }

    public void setAttributeValues(List<AttributeValue> attributeValues) {
        this.attributeValues = attributeValues;
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
                ", version=" + version +
                ", attributeValues=" + attributeValues +
                '}';
    }
}
