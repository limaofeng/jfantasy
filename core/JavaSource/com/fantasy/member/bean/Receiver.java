package com.fantasy.member.bean;

import com.fantasy.common.bean.Area;
import com.fantasy.common.bean.converter.AreaConverter;
import com.fantasy.common.bean.databind.AreaDeserializer;
import com.fantasy.framework.dao.BaseBusEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 收货地址信息
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-9-16 下午4:16:02
 */
@ApiModel("收货地址信息")
@Entity
@Table(name = "MEM_RECEIVER")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "areaStore", "member"})
public class Receiver extends BaseBusEntity {

    private static final long serialVersionUID = 851367820092125804L;

    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = false, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;

    /**
     * 收货人姓名
     */
    @ApiModelProperty("收货人姓名")
    @Column(name = "NAME", length = 20, nullable = false)
    private String name;
    /**
     * 地区存储
     */
    @ApiModelProperty(hidden = true)
    @Column(name = "AREA_STORE", length = 300, nullable = false)
    @Convert(converter = AreaConverter.class)
    private Area area;
    /**
     * 收货地址
     */
    @ApiModelProperty("收货地址")
    @Column(name = "ADDRESS", length = 200, nullable = false)
    private String address;
    /**
     * 邮政编码
     */
    @ApiModelProperty("邮政编码")
    @Column(name = "ZIP_CODE", length = 200, nullable = false)
    private String zipCode;
    /**
     * 手机
     */
    @ApiModelProperty("手机")
    @Column(name = "MOBILE", length = 200, nullable = false)
    private String mobile;
    /**
     * 是否为默认地址
     */
    @ApiModelProperty("是否为默认地址")
    @Column(name = "IS_DEFAULT", nullable = false)
    private Boolean isDefault = false;// 是否默认
    /**
     * 地址对应的用户信息
     */
    @ApiModelProperty(hidden = true)
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "MEMBER_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_SHIP_ADDRESS_MEMBER"))
    private Member member;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    /**
     * 获取地区
     *
     * @return 地区信息
     */
    @ApiModelProperty("地区存储")
    public Area getArea() {
        return this.area;
    }

    /**
     * 设置地区
     *
     * @param area 地区
     */
    @JsonDeserialize(using = AreaDeserializer.class)
    public void setArea(Area area) {
        this.area = area;
    }
}
