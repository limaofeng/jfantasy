package org.jfantasy.filestore.bean;

import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Image implements Comparable<Image> {
    /**
     * 虚拟文件路径
     */
    @ApiModelProperty(value = "文件访问路径")
    private String absolutePath;

    @ApiModelProperty(value = "文件访问域名")
    private String fileManagerId;
    /**
     * 文件名称
     */
    @ApiModelProperty(value = "文件名")
    private String fileName;
    /**
     * 文件后缀名
     */
    @ApiModelProperty(value = "文件后缀名")
    private String ext;
    /**
     * 文件类型
     */
    @ApiModelProperty(value = "文件类型")
    private String contentType;
    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String description;
    /**
     * 文件长度
     */
    @ApiModelProperty(value = "文件长度")
    private Long size;
    /**
     * 文件MD5码
     */
    @ApiModelProperty(value = "文件MD5码")
    private String md5;
    /**
     * 排序
     */
    private Integer sort;

    @Override
    public int hashCode() {
        return new HashCodeBuilder().appendSuper(super.hashCode()).append(this.absolutePath).toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Image) {
            Image image = (Image) o;
            return new EqualsBuilder().appendSuper(super.equals(o)).append(this.absolutePath, image.absolutePath).isEquals();
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

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public String getFileManagerId() {
        return fileManagerId;
    }

    public void setFileManagerId(String fileManagerId) {
        this.fileManagerId = fileManagerId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}
