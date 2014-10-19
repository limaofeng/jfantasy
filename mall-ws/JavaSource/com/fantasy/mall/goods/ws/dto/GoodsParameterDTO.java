package com.fantasy.mall.goods.ws.dto;

/**
 * 商品规格参数
 */
public class GoodsParameterDTO {


    /**
     * 索引Id
     */
    private String id;// ID
    /**
     * 名称
     */
    private String name;
    /**
     * 格式
     */
    private String format;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 备注
     */
    private String remark;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
