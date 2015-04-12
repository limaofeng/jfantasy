package com.fantasy.attr.storage.bean;


import com.fantasy.attr.framework.query.DynaBeanEntityPersister;
import com.fantasy.attr.storage.BaseDynaBean;
import com.fantasy.framework.lucene.annotations.IndexProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Persister;

import javax.persistence.*;

@Entity
@Table(name = "TEST_Article")
@Persister(impl = DynaBeanEntityPersister.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "keywords"})
public class Article extends BaseDynaBean {

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
     * 发布标识
     */
    @Column(name = "ISSUE")
    private Boolean issue = false;

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
                '}';
    }
}
