package com.fantasy.attr.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import com.fantasy.framework.util.common.StringUtil;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

/**
 * 动态属性版本表
 */
@Entity
@Table(name = "ATTR_VERSION",uniqueConstraints = {@UniqueConstraint(columnNames = {"CLASS_NAME","NUMBER"})})
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
    @Column(name = "CLASS_NAME", length = 150)
    private String className;
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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
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


    @JsonIgnore
    public String[] getAttributeSorts(){
        if(StringUtil.isBlank(this.attributeSort)){
           return new String[0];
        }
        return StringUtil.tokenizeToStringArray(this.attributeSort);
    }
}
