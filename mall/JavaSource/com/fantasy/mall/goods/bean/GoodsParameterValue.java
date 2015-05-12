package com.fantasy.mall.goods.bean;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.fantasy.framework.util.common.StringUtil;

import java.util.UUID;

/**
 * 商品参数
 */
public class GoodsParameterValue implements Comparable<GoodsParameterValue> {
    private String id;
    private String name;
    private String value;
    private Integer sort;

    public GoodsParameterValue() {
        this.id = UUID.randomUUID().toString();
    }

    public GoodsParameterValue(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (StringUtil.isNotBlank(id)) {
            this.id = id;
        }
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof GoodsParameterValue) {
            GoodsParameterValue parameterValue = (GoodsParameterValue) o;
            return new EqualsBuilder().appendSuper(super.equals(o)).append(this.getId(), parameterValue.getId()).append(this.getName(), parameterValue.getName()).append(this.getValue(), parameterValue.getValue()).isEquals();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().appendSuper(super.hashCode()).append(this.getId()).append(this.getName()).append(this.getValue()).toHashCode();
    }

    public int compareTo(GoodsParameterValue parameterValue) {
        if (parameterValue.getSort() == null) {
            return 1;
        }
        if (this.getSort() == null) {
            return -1;
        }
        return this.getSort().compareTo(parameterValue.getSort());
    }
}
