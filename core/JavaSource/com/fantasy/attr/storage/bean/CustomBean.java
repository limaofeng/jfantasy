package com.fantasy.attr.storage.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "CUSTOM_BEAN")
public class CustomBean extends BaseBusEntity {

    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = true, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 自定义 bean 的名称
     */
    @Column(name = "NAME")
    private String name;
    /**
     * 自定义类的
     */
    @Column(name = "CLASS_NAME")
    private String className;
    /**
     * 属性
     */
    @ManyToMany(targetEntity = Attribute.class, fetch = FetchType.LAZY)
    @JoinTable(name = "ATTR_VERSION_ATTRIBUTE", joinColumns = @JoinColumn(name = "VERSION_ID"), inverseJoinColumns = @JoinColumn(name = "ATTRIBUTE_ID"), foreignKey = @ForeignKey(name = "FK_VERSION_ATTRIBUTE"))
    private List<Attribute> attributes;

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

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
