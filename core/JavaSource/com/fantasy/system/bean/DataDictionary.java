package com.fantasy.system.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import com.fantasy.framework.error.IgnoreException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.IOException;
import java.util.List;

/**
 * 数据字典类
 * <br/>
 * 该类为了取代Config.java
 */
@ApiModel(value = "数据字典")
@Entity
@Table(name = "SYS_DD")
@IdClass(DataDictionaryKey.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "parent", "children"})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DataDictionary extends BaseBusEntity {

    /**
     * 代码
     */
    @ApiModelProperty("代码")
    @Id
    private String code;
    /**
     * 配置类别
     */
    @ApiModelProperty("配置类别")
    @Id
    private String type;
    /**
     * 名称
     */
    @ApiModelProperty("名称")
    @Column(name = "NAME", length = 50)
    private String name;
    /**
     * 排序字段
     */
    @ApiModelProperty(hidden = true)
    @Column(name = "SORT")
    private Integer sort;
    /**
     * 描述
     */
    @ApiModelProperty("描述")
    @Column(name = "DESCRIPTION", length = 200)
    private String description;
    /**
     * 上级数据字典
     */
    @ApiModelProperty("上级数据字典")
    @ManyToOne(fetch = FetchType.LAZY, cascade = {javax.persistence.CascadeType.REFRESH})
    @JoinColumns(value = {@JoinColumn(name = "PCODE", referencedColumnName = "CODE"), @JoinColumn(name = "PTYPE", referencedColumnName = "TYPE")}, foreignKey = @ForeignKey(name = "FK_SYS_DD_PARENT"))
    private DataDictionary parent;
    /**
     * 下级数据字典
     */
    @ApiModelProperty(hidden = true)
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    @OrderBy("sort ASC")
    private List<DataDictionary> children;

    @ApiModelProperty("KEY")
    @Transient
    @JsonSerialize(using = DataDictionaryKeySerialize.class)
    public DataDictionaryKey getKey() {
        return DataDictionaryKey.newInstance(this.code, this.type);
    }

    @ApiModelProperty("上级数据KEY")
    @Transient
    @JsonSerialize(using = DataDictionaryKeySerialize.class)
    public DataDictionaryKey getParentKey() {
        if (this.getParent() == null) {
            return null;
        }
        return DataDictionaryKey.newInstance(this.parent.code, this.parent.type);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DataDictionary getParent() {
        return parent;
    }

    public void setParent(DataDictionary parent) {
        this.parent = parent;
    }

    public List<DataDictionary> getChildren() {
        return children;
    }

    public void setChildren(List<DataDictionary> children) {
        this.children = children;
    }

    public static class DataDictionaryKeySerialize extends JsonSerializer<DataDictionaryKey> {

        @Override
        public void serialize(DataDictionaryKey dataDictionaryKey, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            try {
                if (dataDictionaryKey == null) {
                    jgen.writeString("");
                } else {
                    jgen.writeString(dataDictionaryKey.getType() + ":" + dataDictionaryKey.getCode());
                }
            } catch (Exception e) {
                jgen.writeString("");
                throw new IgnoreException(e.getMessage(), e);
            }
        }

    }
}
