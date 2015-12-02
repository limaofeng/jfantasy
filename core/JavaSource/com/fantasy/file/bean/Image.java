package com.fantasy.file.bean;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Image extends FileDetail implements Comparable<Image> {

    /**
     * 排序
     */
    private Integer sort;

    @Override
    public int hashCode() {
        return new HashCodeBuilder().appendSuper(super.hashCode()).append(this.getFileDetailKey()).toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Image) {
            Image image = (Image) o;
            return new EqualsBuilder().appendSuper(super.equals(o)).append(this.getFileManagerId(), image.getFileManagerId()).append(this.getAbsolutePath(), image.getAbsolutePath()).isEquals();
        }
        return false;
    }

    public int compareTo(Image image) {
        if (image == null || image.getSort() == null || this.getSort() == null) {
            return -1;
        }
        return this.getSort().compareTo(image.getSort());
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
