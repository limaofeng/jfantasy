package com.fantasy.mall.goods.ws.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class SpecialDTO implements Serializable {


    private static final long serialVersionUID = -8341871540775746477L;

    private Long id;

    private String name; //套餐名称

    private String hour; //小时

    private BigDecimal price;//价格

    private String remark;

    private Integer sort;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }


}
