package com.fantasy.system.bean;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.persistence.Column;
import java.io.Serializable;

public class DataDictionaryKey implements Serializable {

    public static DataDictionaryKey newInstance(String code, String type) {
        return new DataDictionaryKey(code, type);
    }

    /**
     * 代码
     */
    @Column(name = "CODE", length = 50)
    private String code;

    /**
     * 配置类别
     */
    @Column(name = "TYPE", length = 20)
    private String type;

    public DataDictionaryKey() {
    }

    public DataDictionaryKey(String code, String type) {
        super();
        this.code = code;
        this.type = type;
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

    @Override
    public int hashCode() {
        return new HashCodeBuilder().appendSuper(super.hashCode()).append(this.getType()).append(this.getCode()).toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof DataDictionaryKey) {
            DataDictionaryKey key = (DataDictionaryKey) o;
            return new EqualsBuilder().appendSuper(super.equals(o)).append(this.getType(), key.getType()).append(this.getCode(), key.getCode()).isEquals();
        }
        return false;
    }

    @Override
    public String toString() {
        return this.type + ":" + this.code;
    }

}
