package com.fantasy.system.bean;


import com.fantasy.framework.dao.BaseBusEntity;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.IOException;
import java.util.List;

/**
 * 数据字典分类表
 */
@Entity
@Table(name = "SYS_DD_TYPE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "dataDictionaries"})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DataDictionaryType extends BaseBusEntity {

    public static final String PATH_SEPARATOR = ",";// 树路径分隔符

    /**
     * 代码
     */
    @Id
    @Column(name = "CODE", length = 20)
    private String code;
    /**
     * 名称
     */
    @Column(name = "NAME", length = 200)
    private String name;
    @Column(name = "LAYER", nullable = false)
    /**
     * 层级
     */
    private Integer layer;
    /**
     *
     */
    @Column(name = "PATH", nullable = false, length = 200)
    private String path;
    /**
     * 排序字段
     */
    @Column(name = "SORT")
    private Integer sort;
    /**
     * 描述
     */
    @Column(name = "DESCRIPTION", length = 2000)
    private String description;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    @OrderBy("createTime desc")
    @JoinColumn(name = "TYPE",foreignKey = @ForeignKey(name = "FK_SYS_DD_TYPE"))
    private List<DataDictionary> dataDictionaries;

    /**
     * 上级数据字典
     */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "PCODE",foreignKey = @ForeignKey(name = "FK_SYS_DD_TYPE_PID") )
    private DataDictionaryType parent;
    /**
     * 下级数据字典
     */
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    @OrderBy("sort ASC")
    private List<DataDictionaryType> children;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("parentCode")
    @JsonSerialize(using = DataDictionaryTypeParentSerialize.class)
    public DataDictionaryType getParent() {
        return parent;
    }

    public void setParent(DataDictionaryType parent) {
        this.parent = parent;
    }

    public List<DataDictionaryType> getChildren() {
        return children;
    }

    public void setChildren(List<DataDictionaryType> children) {
        this.children = children;
    }

    public List<DataDictionary> getDataDictionaries() {
        return dataDictionaries;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public void setDataDictionaries(List<DataDictionary> dataDictionaries) {
        this.dataDictionaries = dataDictionaries;
    }

    public Integer getLayer() {
        return layer;
    }

    public void setLayer(Integer layer) {
        this.layer = layer;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public static class DataDictionaryTypeParentSerialize extends JsonSerializer<DataDictionaryType> {

        @Override
        public void serialize(DataDictionaryType ddt, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeString(ddt.getCode());
        }

    }
}