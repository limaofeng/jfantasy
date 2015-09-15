package com.fantasy.system.bean;


import com.fantasy.framework.dao.BaseBusEntity;
import com.fantasy.system.bean.databind.DataDictionaryTypeDeserializer;
import com.fantasy.system.bean.databind.DataDictionaryTypeSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.List;

/**
 * 数据字典分类表
 */
@ApiModel(value = "数据字典分类")
@Entity
@Table(name = "SYS_DD_TYPE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "dataDictionaries", "children"})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DataDictionaryType extends BaseBusEntity {

    public static final String PATH_SEPARATOR = ",";// 树路径分隔符

    /**
     * 代码
     */
    @ApiModelProperty("代码")
    @Id
    @Column(name = "CODE", length = 20)
    private String code;
    /**
     * 名称
     */
    @ApiModelProperty("名称")
    @Column(name = "NAME", length = 200)
    private String name;
    /**
     * 层级
     */
    @ApiModelProperty("层级")
    @Column(name = "LAYER", nullable = false)
    private Integer layer;
    /**
     *
     */
    @ApiModelProperty("路径")
    @Column(name = "PATH", nullable = false, length = 200)
    private String path;
    /**
     * 排序字段
     */
    @ApiModelProperty("排序字段")
    @Column(name = "SORT")
    private Integer sort;
    /**
     * 描述
     */
    @ApiModelProperty("描述")
    @Column(name = "DESCRIPTION", length = 2000)
    private String description;
    @ApiModelProperty(hidden = true)
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    @OrderBy("createTime desc")
    @JoinColumn(name = "TYPE", foreignKey = @ForeignKey(name = "FK_SYS_DD_TYPE"))
    private List<DataDictionary> dataDictionaries;
    /**
     * 上级数据字典
     */
    @ApiModelProperty("上级数据字典分类")
    @JsonProperty("parentCode")
    @JsonSerialize(using = DataDictionaryTypeSerializer.class)
    @JsonDeserialize(using = DataDictionaryTypeDeserializer.class)
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "PCODE", foreignKey = @ForeignKey(name = "FK_SYS_DD_TYPE_PID"))
    private DataDictionaryType parent;
    /**
     * 下级数据字典
     */
    @ApiModelProperty(hidden = true)
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    @OrderBy("sort ASC")
    private List<DataDictionaryType> children;

    public DataDictionaryType() {
    }

    public DataDictionaryType(String code) {
        this.code = code;
    }

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

}