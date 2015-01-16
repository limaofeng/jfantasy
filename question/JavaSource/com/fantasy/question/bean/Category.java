package com.fantasy.question.bean;


import com.fantasy.framework.dao.BaseBusEntity;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.IOException;
import java.util.List;

@Entity
@Table(name = "YR_CATEGORY")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Category extends BaseBusEntity {

    public static final String PATH_SEPARATOR = ",";// 树路径分隔符

    /**
     * 分类ID
     */
    @Id
    @Column(name = "ID", insertable = true, updatable = false)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 分类名称
     */
    @Column(name = "NAME", nullable = false)
    private String name;
    /**
     * 分类标识
     */
    @Column(name = "SIGN", nullable = false, unique = true)
    private String sign;
    /**
     * 页面关键词
     */
    @Column(name = "META_KEYWORDS", length = 3000)
    private String metaKeywords;// 页面关键词
    /**
     * 页面描述
     */
    @Column(name = "META_DESCRIPTION", length = 3000)
    private String metaDescription;// 页面描述
    /**
     * 排序
     */
    @Column(name = "SORT")
    private Integer sort;// 排序
    /**
     * 树路径
     */
    @Column(name = "PATH", nullable = false, length = 200)
    private String path;// 树路径
    /**
     * 层级
     */
    @Column(name = "LAYER", nullable = false)
    private Integer layer;
    /**
     * 上级分类
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "P_ID",foreignKey = @ForeignKey(name = "FK_CATEGORY_PARENT"))
    private Category parent;
    /**
     * 下级分类
     */
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE })
    private List<Category> children;

    /**
     * 问题
     */
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE })
    private List<Question> questions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getMetaKeywords() {
        return metaKeywords;
    }

    public void setMetaKeywords(String metaKeywords) {
        this.metaKeywords = metaKeywords;
    }

    public String getMetaDescription() {
        return metaDescription;
    }

    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getLayer() {
        return layer;
    }

    public void setLayer(Integer layer) {
        this.layer = layer;
    }

    @JsonProperty("parentId")
    @JsonSerialize(using = CategoryParentSerialize.class)
    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public List<Category> getChildren() {
        return children;
    }

    public void setChildren(List<Category> children) {
        this.children = children;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public static class CategoryParentSerialize extends JsonSerializer<Category> {
        @Override
        public void serialize(Category category, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
            jgen.writeString(category.getId() != null ? category.getId().toString() : "");
        }
    }
}
