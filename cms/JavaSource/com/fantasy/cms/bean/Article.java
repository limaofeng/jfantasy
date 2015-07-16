package com.fantasy.cms.bean;

import com.fantasy.attr.framework.query.DynaBeanEntityPersister;
import com.fantasy.attr.storage.BaseDynaBean;
import com.fantasy.framework.lucene.annotations.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Persister;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import javax.persistence.Entity;
import java.io.IOException;

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
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "keywords"})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Article extends BaseDynaBean {

    private static final long serialVersionUID = 3480217915594201004L;

    enum Type {
        /**
         * 原创
         */
        original,
        /**
         * 转载
         */
        transfer,
        /**
         * 链接
         */
        link
    }

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
    @JsonSerialize(using = ContentSerialize.class)
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

    public static class ContentSerialize extends JsonSerializer<Content> {

        @Override
        public void serialize(Content content, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeString(content.toString());
        }

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
