package com.fantasy.framework.lucene.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import com.fantasy.framework.lucene.annotations.IndexProperty;
import com.fantasy.framework.lucene.annotations.Indexed;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Indexed
@Table(name = "TEST_NEWS")
public class News extends BaseBusEntity{

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
    @Lob
    @Column(name = "CONTENT")
    private String content;

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
}
