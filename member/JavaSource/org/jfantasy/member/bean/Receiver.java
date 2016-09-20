package org.jfantasy.member.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.jfantasy.common.Area;
import org.jfantasy.common.converter.AreaConverter;
import org.jfantasy.common.databind.AreaDeserializer;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.spring.validation.RESTful;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * 收货地址信息
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-9-16 下午4:16:02
 */
@Entity
@Table(name = "MEM_RECEIVER")
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler", "areaStore", "member"})
public class Receiver extends BaseBusEntity {

    private static final long serialVersionUID = 851367820092125804L;

    @Id
    @Column(name = "ID", nullable = false, updatable = false, precision = 22, scale = 0)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "test:id")
    @TableGenerator(name = "test:id", table = "sys_sequence",pkColumnName = "gen_name",valueColumnName = "gen_value")
    private Long id;

    /**
     * 收货人姓名
     */
    @NotNull(groups = {RESTful.POST.class, RESTful.PUT.class})
    @Column(name = "NAME", length = 20, nullable = false)
    private String name;
    /**
     * 地区存储
     */
    @NotNull(groups = {RESTful.POST.class, RESTful.PUT.class})
    @Column(name = "AREA_STORE", length = 300, nullable = false)
    @Convert(converter = AreaConverter.class)
    private Area area;
    /**
     * 收货地址
     */
    @NotNull(groups = {RESTful.POST.class, RESTful.PUT.class})
    @Column(name = "ADDRESS", length = 200, nullable = false)
    private String address;
    /**
     * 邮政编码
     */
    @Column(name = "ZIP_CODE", length = 200)
    private String zipCode;
    /**
     * 手机
     */
    @NotNull(groups = {RESTful.POST.class, RESTful.PUT.class})
    @Column(name = "MOBILE", length = 200, nullable = false)
    private String mobile;
    /**
     * 是否为默认地址
     */
    @JsonProperty("default")
    @NotNull(groups = {RESTful.POST.class, RESTful.PUT.class})
    @Column(name = "IS_DEFAULT", nullable = false)
    private Boolean isDefault = false;// 是否默认
    /**
     * 地址对应的用户信息
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "MEMBER_ID", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "FK_RECEIVER_MEMBER"))
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

    @Transient
    public void setMemberId(Long memberId) {
        this.setMember(new Member(memberId));
    }

    @Transient
    @NotNull(groups = RESTful.POST.class)
    public Long getMemberId() {
        return this.member == null ? null : this.member.getId();
    }

}
