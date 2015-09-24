package com.fantasy.common.order;

import com.fantasy.common.bean.Area;
import com.fantasy.common.bean.databind.AreaDeserializer;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.jackson.JSON;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 用户地址信息
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-9-16 下午4:16:02
 */
@ApiModel("订单配送地址")
@JsonIgnoreProperties({"areaStore"})
public class ShipAddress {
    /**
     * 姓名
     */
    @ApiModelProperty("姓名")
    private String name;
    /**
     * 地区存储
     */
    @ApiModelProperty(hidden = true)
    private String areaStore;
    /**
     * 街道
     */
    @ApiModelProperty("街道详细地址")
    private String address;
    /**
     * 邮政编码
     */
    @ApiModelProperty("邮政编码")
    private String zipCode;
    /**
     * 电话
     */
    @ApiModelProperty("电话")
    private String phone;
    /**
     * 手机
     */
    @ApiModelProperty("手机")
    private String mobile;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAreaStore() {
        return areaStore;
    }

    public void setAreaStore(String areaStore) {
        this.areaStore = areaStore;
    }

    /**
     * 获取地区
     *
     * @return 地区信息
     */
    @ApiModelProperty("地区存储")
    public Area getArea() {
        if (StringUtil.isBlank(this.areaStore)) {
            return null;
        }
        return JSON.deserialize(this.areaStore, Area.class);
    }

    /**
     * 设置地区
     *
     * @param area 地区
     */
    @JsonDeserialize(using = AreaDeserializer.class)
    public void setArea(Area area) {
        if (area == null) {
            this.areaStore = null;
            return;
        }
        this.areaStore = JSON.serialize(area);
    }

}
