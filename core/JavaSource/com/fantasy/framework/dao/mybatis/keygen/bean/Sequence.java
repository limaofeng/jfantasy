package com.fantasy.framework.dao.mybatis.keygen.bean;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 序列
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-8-24 上午10:35:57
 */
@Entity
@Table(name = "SYS_SEQUENCE")
public class Sequence implements Serializable {
    private static final long serialVersionUID = -3013203287222083845L;

    /**
     * 序列名称
     */
    @Id
    @Column(name = "GEN_NAME")
    private String key;

    /**
     * 序列值
     */
    @Column(name = "GEN_VALUE", nullable = false)
    private Long value = 0L;

    /**
     * 原始值
     */
    @Transient
    private Long originalValue;

    public Sequence() {
    }

    public Sequence(String key, long poolSize) {
        this.key = key;
        this.value = poolSize;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getValue() {
        return this.value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public Long getOriginalValue() {
        return originalValue;
    }

    public void setOriginalValue(Long originalValue) {
        this.originalValue = originalValue;
    }

}