package com.fantasy.attr.storage.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Table;
import java.util.List;

/**
 * 属性表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2012-11-4 下午05:54:48
 */
@Entity
@Table(name = "ATTR_ATTRIBUTE")
@JsonIgnoreProperties("versions")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Attribute extends BaseBusEntity {

    private static final long serialVersionUID = -5280686526831631282L;

    @Id
    @Column(name = "ID", updatable = false)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 属性编码(用于自定义界面时，方便操作) 逻辑上，一组配置(如某个栏目扩展字段)中不应该出现两个相同的code
     */
    @Column(name = "CODE", length = 50)
    private String code;
    /**
     * 属性名称
     */
    @Column(name = "NAME", length = 50)
    private String name;
    /**
     * 非临时属性<br/>
     * 临时属性，会在Version删除后自动删除。
     */
    @Column(name = "NOT_TEMPORARY")
    private Boolean notTemporary = true;
    /**
     * 非空
     */
    @Column(name = "NON_NULL")
    private Boolean nonNull = false;
    /**
     * 类型
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ATTRIBUTE_TYPE_ID", foreignKey = @ForeignKey(name = "FK_ATTR_ATTRIBUTE_TYPE"))
    private AttributeType attributeType;
    /**
     * 描述
     */
    @Column(name = "DESCRIPTION", length = 2000)
    private String description;
    /**
     * 属性对于的值集合
     */
    @OneToMany(mappedBy = "attribute", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    private List<AttributeValue> attributeValues;

    @ManyToMany(targetEntity = AttributeVersion.class, fetch = FetchType.LAZY)
    @JoinTable(name = "ATTR_VERSION_ATTRIBUTE", joinColumns = @JoinColumn(name = "ATTRIBUTE_ID"), inverseJoinColumns = @JoinColumn(name = "VERSION_ID"), foreignKey = @ForeignKey(name = "FK_ATTRIBUTE_VERSION"))
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private List<AttributeVersion> versions;

    public Attribute() {
    }

    public Attribute(String code) {
        this.code = code;
    }

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

    public AttributeType getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(AttributeType attributeType) {
        this.attributeType = attributeType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getNonNull() {
        return nonNull;
    }

    public void setNonNull(Boolean nonNull) {
        this.nonNull = nonNull;
    }

    public List<AttributeValue> getAttributeValues() {
        return attributeValues;
    }

    public void setAttributeValues(List<AttributeValue> attributeValues) {
        this.attributeValues = attributeValues;
    }

    public Boolean getNotTemporary() {
        return notTemporary;
    }

    public void setNotTemporary(Boolean notTemporary) {
        this.notTemporary = notTemporary;
    }

    public List<AttributeVersion> getVersions() {
        return versions;
    }

    public void setVersions(List<AttributeVersion> versions) {
        this.versions = versions;
    }

    @Override
    public String toString() {
        return "Attribute{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", notTemporary=" + notTemporary +
                ", nonNull=" + nonNull +
                ", attributeType=" + attributeType +
                ", description='" + description + '\'' +
                '}';
    }
}
