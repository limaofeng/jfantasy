package com.fantasy.file.bean;

import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.regexp.RegexpUtil;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * 商品预览时的图片
 *
 * @author 李茂峰
 * @version 1.0
 * @功能描述
 * @since 2013-10-21 下午4:57:33
 */
@JsonIgnoreProperties(value = {"fileManagerId", "realPath", "md5", "folder", "creator", "createTime", "modifier", "modifyTime"})
public class ImageFile extends FileDetail implements Comparable<ImageFile> {

    private static final long serialVersionUID = 5747517861013219422L;
    /**
     * 排序
     */
    private Integer sort;

    @Override
    public int compareTo(ImageFile imageFile) {
        return this.getSort().compareTo(imageFile.getSort());
    }

    public Integer getSort() {
        return ObjectUtil.defaultValue(sort,1);
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    // 获得商品图片（原）路径
    public String getSourceImagePath() {
        return this.getAbsolutePath();
    }

    // 获得商品图片路径
    public String getImagePath(String confine) {
        return RegexpUtil.replace(this.getAbsolutePath(), "[.][^.]{1,}$", "_" + confine + "$0");
    }

}
