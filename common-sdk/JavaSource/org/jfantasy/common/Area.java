package org.jfantasy.common;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("地区信息")
public class Area {

    private String id;
    @ApiModelProperty("名称")
    private String name;// 名称
    @ApiModelProperty("完整地区名称")
    private String displayName;// 完整地区名称
    @ApiModelProperty(hidden = true)
    private String path;// 路径
    @ApiModelProperty("标签")
    private String[] tags;
    @ApiModelProperty("层级")
    private Integer layer;// 层级
    @ApiModelProperty("排序")
    private Integer sort;// 排序

    public Area() {
    }

    private Area(String id) {
        this();
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getLayer() {
        return layer;
    }

    public void setLayer(Integer layer) {
        this.layer = layer;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

}
