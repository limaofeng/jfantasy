package org.jfantasy.springboot.bean;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;


@Entity
@Table(name = "TEST_ARTICLE")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler", "keywords", "category", "content", "target"})
public class Article extends BaseBusEntity {

    @Id
    @Column(name = "ID", precision = 22)
    @GeneratedValue(generator = "test_article_gen")
    @TableGenerator(name = "test_article_gen", table = "sys_sequence", pkColumnName = "gen_name", pkColumnValue = "test_article:id", valueColumnName = "gen_value")
    private Long id;
    /**
     * 商品编号
     */
    @Column(name = "SN", nullable = false, unique = true, updatable = false)
    @GenericGenerator(name = "serialnumber", strategy = "serialnumber", parameters = {@Parameter(name = "expression", value = "'A' + #DateUtil.format('yyyyMMdd') + #StringUtil.addZeroLeft(#SequenceInfo.nextValue('TEST-SN'), 5)")})
    private String sn;
    /**
     * 文章标题
     */
    @Column(name = "TITLE")
    private String title;
    /**
     * 摘要
     */
    @Column(name = "SUMMARY", length = 500)
    private String summary;
    /**
     * 关键词
     */
    @Column(name = "KEYWORDS")
    private String keywords;
    /**
     * 文章正文
     */
    @Lob
    @Column(name = "CONTENT")
    private String content;
    /**
     * 作者
     */
    @Column(name = "AUTHOR")
    private String author;

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

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
