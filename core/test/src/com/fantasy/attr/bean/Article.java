package com.fantasy.attr.bean;


import com.fantasy.attr.DynaBean;
import com.fantasy.attr.DynaBeanEntityPersister;
import com.fantasy.framework.dao.BaseBusEntity;
import com.fantasy.framework.lucene.annotations.IndexProperty;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Persister;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "TEST_Article")
@Persister(impl = DynaBeanEntityPersister.class)
public class Article extends BaseBusEntity implements DynaBean {

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

    @Column(name = "ISSUE")
    private Boolean issue = false;

    /**
     * 数据版本
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VERSION_ID", foreignKey = @ForeignKey(name = "FK_CMS_ARTICLE_VERSION"))
    private AttributeVersion version;
    /**
     * 动态属性集合。
     */
    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumns(value = {@JoinColumn(name = "TARGET_ID", referencedColumnName = "ID"), @JoinColumn(name = "VERSION_ID", referencedColumnName = "VERSION_ID")})
    private List<AttributeValue> attributeValues;

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
                ", attributeValues=" + attributeValues +
                '}';
    }
}
