package com.fantasy.mall.member.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import com.fantasy.member.bean.Member;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 用户地址信息
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-9-16 下午4:16:02
 */
public class ShipAddress extends BaseBusEntity {

    private static final long serialVersionUID = 851367820092125804L;

    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = false, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;

    /**
     * 姓名
     */
    @Column(name = "name", length = 20)
    private String name;

    /**
     * 国家
     */
    @Column(name = "COUNTRY", length = 20)
    private String country;
    /**
     * 省份
     */
    @Column(name = "PROVINCE", length = 20)
    private String province;
    /**
     * 城市
     */
    @Column(name = "CITY", length = 20)
    private String city;
    /**
     * 街道
     */
    @Column(name = "STREET", length = 200)
    private String street;
    /**
     * 邮政编码
     */
    @Column(name = "ZIP_CODE", length = 200)
    private String zipCode;
    /**
     * 电话
     */
    @Column(name = "PHONE", length = 200)
    private String phone;
    /**
     * 手机
     */
    @Column(name = "MOBILE", length = 200)
    private String mobile;
    /**
     * 地址对应的用户信息
     */
    @ManyToOne(cascade = {CascadeType.REMOVE})
    @JoinColumn(name = "MEMBER_ID",foreignKey = @ForeignKey(name = "FK_SHIP_ADDRESS_MEMBER") )

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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
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

}
