package com.fantasy.attr.storage.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

/**
 * 动态属性版本表
 */
@Entity
@Table(name = "ATTR_VERSION",uniqueConstraints = {@UniqueConstraint(columnNames = {"TARGET_CLASS_NAME","NUMBER"})})
@JsonIgnoreProperties({"hibernateLazyInitializer", "attributes"})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AttributeVersion extends BaseBusEntity {

    @Id
    @Column(name = "ID", updatable = false)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 对应的className
     */
    @Column(name = "TARGET_CLASS_NAME", length = 150)
    private String targetClassName;
    /**
     * 版本号
     */
    @Column(name = "NUMBER", length = 50)
    private String number;
    /**
     * 属性
     */
    @ManyToMany(targetEntity = Attribute.class, fetch = FetchType.LAZY)
    @JoinTable(name = "ATTR_VERSION_ATTRIBUTE", joinColumns = @JoinColumn(name = "VERSION_ID"), inverseJoinColumns = @JoinColumn(name = "ATTRIBUTE_ID"),foreignKey = @ForeignKey(name = "FK_VERSION_ATTRIBUTE"))
    private List<Attribute> attributes;
    /**
     * 品牌在分类中的排序规则
     */
    @Column(name = "ATTRIBUTE_SORT", length = 100)
    private String attributeSort;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTargetClassName() {
        return targetClassName;
    }

    public void setTargetClassName(String targetClassName) {
        this.targetClassName = targetClassName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public String getAttributeSort() {
        return attributeSort;
    }

    public void setAttributeSort(String attributeSort) {
        this.attributeSort = attributeSort;
    }

    @Transient
    public String getClassName(){
        return this.getTargetClassName() + ClassUtil.CGLIB_CLASS_SEPARATOR + this.getNumber();
    }

    @JsonIgnore
    public String[] getAttributeSorts(){
        if(StringUtil.isBlank(this.attributeSort)){
           return new String[0];
        }
        return StringUtil.tokenizeToStringArray(this.attributeSort);
    }
}
