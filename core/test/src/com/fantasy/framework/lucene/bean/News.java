package org.jfantasy.framework.lucene.bean;

import org.jfantasy.attr.framework.query.DynaBeanEntityPersister;
import org.jfantasy.attr.storage.BaseDynaBean;
import org.jfantasy.framework.lucene.annotations.IndexProperty;
import org.jfantasy.framework.lucene.annotations.Indexed;
import org.jfantasy.framework.util.common.StringUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import opensource.jpinyin.PinyinFormat;
import opensource.jpinyin.PinyinHelper;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Persister;

import javax.persistence.*;

@Entity
@Indexed
@Table(name = "TEST_NEWS")
@Persister(impl = DynaBeanEntityPersister.class)
public class News extends BaseDynaBean {

    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = true, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 文章标题
     */
    @IndexProperty(analyze = true, store = true)
    @Column(name = "TITLE")//@Convert(converter = DESConverter.class)
    private String title;
    /**
     * 摘要
     */
    @IndexProperty(analyze = true, store = true)
    @Column(name = "SUMMARY",length = 500)
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

    /**
     * 拼音首字母
     *
     * @return String
     */
    @JsonIgnore
    @Transient
    @IndexProperty(analyze = true, store = true)
    public String getPinyin() {
        if (StringUtil.isBlank(this.getTitle())) {
            return null;
        }
        return PinyinHelper.getShortPinyin(this.getTitle());
    }

    /**
     * 全拼
     *
     * @return String
     */
    @JsonIgnore
    @Transient
    @IndexProperty(analyze = true, store = true)
    public String getQpin() {
        if (StringUtil.isBlank(this.getTitle())) {
            return null;
        }
        return PinyinHelper.convertToPinyinString(this.getTitle(), "", PinyinFormat.WITHOUT_TONE);
    }

}
