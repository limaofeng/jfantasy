package com.fantasy.attr.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

/**
 * 属性类型表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-3-15 下午11:57:36
 */
@Entity
@Table(name = "ATTR_ATTRIBUTE_TYPE")
public class AttributeType extends BaseBusEntity {

    private static final long serialVersionUID = 8249475206812495215L;

    @Id
    @Column(name = "ID", updatable = false)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 属性类型 (java className)
     */
    @Column(name = "DATA_TYPE", length = 200, unique = true)
    private String dataType;
    /**
     * 关联的外键字段
     */
    @Column(name = "FOREIGN_KEY", length = 20)
    private String foreignKey;
    /**
     * 属性类型对应的转换器
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONVERTER_ID",foreignKey = @ForeignKey(name = "FK_ATTRIBUTE_TYPE_CONVERTER") )
    private Converter converter;
    /**
     * 属性类型名称
     */
    @Column(name = "NAME", length = 200)
    private String name;
    /**
     * 属性描述信息
     */
    @Column(name = "DESCRIPTION", length = 2000)
    private String description;

    @OneToMany(mappedBy = "attributeType", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    private List<Attribute> attributes;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Converter getConverter() {
        return converter;
    }

    public void setConverter(Converter converter) {
        this.converter = converter;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public String getForeignKey() {
        return foreignKey;
    }

    public void setForeignKey(String foreignKey) {
        this.foreignKey = foreignKey;
    }
}
