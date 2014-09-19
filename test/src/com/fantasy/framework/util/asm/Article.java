package com.fantasy.framework.util.asm;


import com.fantasy.attr.bean.AttributeValue;
import com.fantasy.attr.util.VersionUtil;
import com.fantasy.framework.lucene.annotations.Indexed;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Indexed
@Entity
@Table(name = "CMS_ARTICLE")
public class Article {

    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = true, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    @JoinColumns(value = {@JoinColumn(name = "TARGET_ID", referencedColumnName = "ID"), @JoinColumn(name = "VERSION_ID", referencedColumnName = "VERSION_ID")})
    protected List<AttributeValue> attributeValues;

    private String testOp;

    private String user;

    public List<AttributeValue> getAttributeValues() {
        return attributeValues;
    }

    public void setAttributeValues(List<AttributeValue> attributeValues) {
        this.attributeValues = attributeValues;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTestOp(String test) {
        this.testOp = (String) VersionUtil.saveValue(this.attributeValues, "testOp", test);
    }

    public String getTestOp() {
        if (this.testOp != null) {
            return this.testOp;
        }
        return (String) VersionUtil.getValue(this.attributeValues, "testOp");
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

}
