package com.fantasy.framework.util.asm;


import com.fantasy.attr.bean.AttributeValue;
import com.fantasy.framework.lucene.annotations.Indexed;

import java.util.List;

@Indexed
public class Article {

    private Long id;

    protected List<AttributeValue> attributeValues;

    private String testOp;

    private String user;

    private boolean tflag;

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

    public String getTestOp() {
        return testOp;
    }

    public void setTestOp(String testOp) {
        this.testOp = testOp;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public boolean isTflag() {
        return tflag;
    }

    public void setTflag(boolean tflag) {
        this.tflag = tflag;
    }
}
