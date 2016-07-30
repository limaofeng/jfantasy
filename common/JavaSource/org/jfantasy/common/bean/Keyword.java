package org.jfantasy.common.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.lucene.annotations.IndexProperty;
import org.jfantasy.framework.lucene.annotations.Indexed;
import org.jfantasy.framework.util.common.StringUtil;

import javax.persistence.*;

/**
 * 自定义关键词表(相当于词库)
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-12-23 下午3:25:46
 */
@Indexed
@Entity
@Table(name = "KEYWORDS")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
public class Keyword extends BaseBusEntity {

    private static final long serialVersionUID = 3340627269786275436L;

    @Id
    @Column(name = "ID", updatable = false)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 类型
     */
    @IndexProperty
    @Column(name = "TYPE")
    private String type;
    /**
     * 词组
     */
    @IndexProperty
    @Column(name = "WORDS")
    private String words;
    /**
     * 所有者
     */
    @IndexProperty
    @Column(name = "OWNER")
    private String owner;

    public Keyword() {
    }

    public Keyword(String type, String keyword) {
        this.type = type;
        this.words = keyword;
        this.owner = "public";
    }

    public Keyword(String type, String keyword,String owner) {
        this.type = type;
        this.words = keyword;
        this.owner = owner;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
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
        if (StringUtil.isBlank(this.getWords())) {
            return null;
        }
        return PinyinHelper.getShortPinyin(this.getWords());
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
        if (StringUtil.isBlank(this.getWords())) {
            return null;
        }
        return PinyinHelper.convertToPinyinString(this.getWords(), "", PinyinFormat.WITHOUT_TONE);
    }

}
