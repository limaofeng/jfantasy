package com.fantasy.mall.goods.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fantasy.file.bean.FileDetail;
import com.fantasy.framework.util.regexp.RegexpUtil;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 商品预览时的图片
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-10-21 下午4:57:33
 */
@JsonIgnoreProperties(value = {"realPath", "md5", "folder", "creator", "createTime", "modifier", "modifyTime", "sourceImagePath", "bigImagePath", "smallImagePath", "thumbnailImagePath"})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class GoodsImage extends FileDetail implements Comparable<GoodsImage> {

    private static final long serialVersionUID = 5747517861013219422L;
    /**
     * 排序
     */
    private Integer sort;

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * 获得商品图片（原）路径
     *
     * @return String
     */
    public String getSourceImagePath() {
        return this.getAbsolutePath();
    }

    /**
     * 获得商品图片（大）路径
     *
     * @return String
     */
    public String getBigImagePath() {
        return RegexpUtil.replace(this.getAbsolutePath(), "[.][a-zA-Z]{1,}$", "_438x248$0");
    }

    /**
     * 获得商品图片（小）路径
     *
     * @return String
     */
    public String getSmallImagePath() {
        return RegexpUtil.replace(this.getAbsolutePath(), "[.][a-zA-Z]{1,}$", "_250x153$0");
    }

    /**
     * 获得商品图片（缩略）路径
     *
     * @return String
     */
    public String getThumbnailImagePath() {
        return RegexpUtil.replace(this.getAbsolutePath(), "[.][a-zA-Z]{1,}$", "_94x53$0");
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().appendSuper(super.hashCode()).append(this.getFileDetailKey()).toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof GoodsImage) {
            GoodsImage goodsImage = (GoodsImage) o;
            return new EqualsBuilder().appendSuper(super.equals(o)).append(this.getFileManagerId(), goodsImage.getFileManagerId()).append(this.getAbsolutePath(), goodsImage.getAbsolutePath()).isEquals();
        }
        return false;
    }

    public int compareTo(GoodsImage goodsImage) {
        if (goodsImage == null || goodsImage.getSort() == null || this.getSort() == null) {
            return -1;
        }
        return this.getSort().compareTo(goodsImage.getSort());
    }

}
