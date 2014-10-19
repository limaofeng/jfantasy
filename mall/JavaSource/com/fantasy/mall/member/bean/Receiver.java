package com.fantasy.mall.member.bean;

import com.fantasy.common.bean.Area;
import com.fantasy.framework.dao.BaseBusEntity;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.member.bean.Member;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 收货地址信息
 *
 * @author 李茂峰
 * @version 1.0
 * @功能描述
 * @since 2013-9-16 下午4:16:02
 */
@Entity
@Table(name = "MALL_MEM_RECEIVER")
@JsonIgnoreProperties({"hibernateLazyInitializer", "areaStore", "member"})
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
    @Column(name = "NAME", length = 20, nullable = false)
    private String name;
    /**
     * 地区存储
     */
    @Column(name = "AREA_STORE", length = 300, nullable = false)
    private String areaStore;
    /**
     * 收货地址
     */
    @Column(name = "ADDRESS", length = 200, nullable = false)
    private String address;
    /**
     * 邮政编码
     */
    @Column(name = "ZIP_CODE", length = 200, nullable = false)
    private String zipCode;
    /**
     * 电话
     */
    @Column(name = "PHONE", length = 200, nullable = false)
    private String phone;
    /**
     * 手机
     */
    @Column(name = "MOBILE", length = 200, nullable = false)
    private String mobile;
    /**
     * 是否为默认地址
     */
    @Column(name = "IS_DEFAULT", nullable = false)
    private Boolean isDefault = false;// 是否默认
    /**
     * 地址对应的用户信息
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "MEMBER_ID", nullable = false,foreignKey = @ForeignKey(name = "FK_SHIP_ADDRESS_MEMBER"))

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

    public String getAreaStore() {
        return areaStore;
    }

    @TypeConversion(key = "areaStore", converter = "com.fantasy.common.bean.converter.AreaStoreConverter")
    public void setAreaStore(String areaStore) {
        this.areaStore = areaStore;
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
     * @return
     * @功能描述
     */
    @Transient
    public Area getArea() {
        if (StringUtil.isBlank(this.areaStore)) {
            return null;
        }
        return JSON.deserialize(this.areaStore, Area.class);
    }

    /**
     * 设置地区
     *
     * @param area
     * @功能描述
     */
    @Transient
    public void setArea(Area area) {
        if (area == null) {
            this.areaStore = null;
            return;
        }
        this.areaStore = JSON.serialize(area);
    }
}
