package com.fantasy.attr.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 属性值表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2012-11-4 下午06:10:12
 */
@Entity
@Table(name = "ATTR_ATTRIBUTE_VALUE", uniqueConstraints = {@UniqueConstraint(columnNames = {"VERSION_ID", "ATTRIBUTE_ID", "TARGET_ID"})})
public class AttributeValue extends BaseBusEntity {

    private static final long serialVersionUID = 5155306149647104462L;

    @Id
    @Column(name = "ID", updatable = false)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 属性对象
     */
    @JoinColumn(name = "ATTRIBUTE_ID", foreignKey = @ForeignKey(name = "FK_ATTRIBUTE_VALUE_ATTRIBUTE"))
    @ManyToOne(fetch = FetchType.EAGER)
    private Attribute attribute;
    /**
     * 数据版本
     */
    @JoinColumn(name = "VERSION_ID", foreignKey = @ForeignKey(name = "FK_ATTRIBUTE_VALUE_VERSION"))
    @ManyToOne(fetch = FetchType.EAGER)
    private AttributeVersion version;
    /**
     * 关联对象对应的id
     */
    @Column(name = "TARGET_ID", nullable = false)
    private Long targetId;
    /**
     * 属性值
     */
    @Column(name = "VALUE", length = 3000)
    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public AttributeVersion getVersion() {
        return version;
    }

    public void setVersion(AttributeVersion version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "AttributeValue{" +
                "attribute=" + attribute +
                ", value='" + value + '\'' +
                ", targetId=" + targetId +
                '}';
    }
}
