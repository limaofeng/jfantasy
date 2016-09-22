package org.jfantasy.member.bean;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.jfantasy.filestore.Image;
import org.jfantasy.filestore.converter.ImageConverter;
import org.jfantasy.filestore.databind.ImageDeserializer;
import org.jfantasy.framework.dao.hibernate.converter.MapConverter;
import org.jfantasy.security.bean.enums.Sex;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户详细信息表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-3-25 下午03:43:54
 */
@Entity
@Table(name = "MEM_MEMBER_DETAILS")
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler", "member"})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MemberDetails implements Serializable {

    private static final long serialVersionUID = -5738290484268799275L;

    @Id
    @Column(name = "MEMBER_ID", nullable = false, updatable = false, precision = 22)
    @GenericGenerator(name = "pkGenerator", strategy = "foreign", parameters = {@Parameter(name = "property", value = "member")})
    @GeneratedValue(generator = "pkGenerator")
    private Long memberId;
    /**
     * 姓名
     */
    @Column(name = "NAME", length = 20)
    private String name;
    /**
     * 性别
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "SEX", length = 20)
    private Sex sex;
    /**
     * 生日
     */
    @Column(name = "BIRTHDAY")
    private Date birthday;
    /**
     * 移动电话
     */
    @Column(name = "MOBILE", length = 20)
    private String mobile;
    /**
     * 固定电话
     */
    @Column(name = "TEL", length = 20)
    private String tel;
    /**
     * E-mail
     */
    @Column(name = "EMAIL", length = 50)
    private String email;
    /**
     * 邮箱是否验证
     */
    @Column(name = "MAIL_VALID", nullable = false)
    private Boolean mailValid;
    /**
     * 手机号是否验证
     */
    @Column(name = "MOBILE_VALID", nullable = false)
    private Boolean mobileValid;
    /**
     * 网址
     */
    @Column(name = "WEBSITE", length = 50)
    private String website;
    /**
     * 描述信息
     */
    @Column(name = "DESCRIPTION")
    private String description;
    /**
     * 会员等级
     */
    @Column(name = "LEVEL")
    private Long level;
    /**
     * 用户头像
     */
    @Column(name = "AVATAR", length = 500)
    @Convert(converter = ImageConverter.class)
    private Image avatar;

    @OneToOne(fetch = FetchType.LAZY, targetEntity = Member.class, mappedBy = "details")
    private Member member;

    @Convert(converter = MapConverter.class)
    @Column(name = "PROPERTIES", columnDefinition = "Text")
    private Map<String, Object> properties;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Long getMemberId() {
        return this.memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Boolean getMailValid() {
        return mailValid;
    }

    public void setMailValid(Boolean mailValid) {
        this.mailValid = mailValid;
    }

    public Boolean getMobileValid() {
        return mobileValid;
    }

    public void setMobileValid(Boolean mobileValid) {
        this.mobileValid = mobileValid;
    }

    @JsonDeserialize(using = ImageDeserializer.class)
    public void setAvatar(Image image) {
        this.avatar = image;
    }

    public Image getAvatar() {
        return this.avatar;
    }

    public Long getLevel() {
        return level;
    }

    public void setLevel(Long level) {
        this.level = level;
    }

    @JsonAnyGetter
    public Map<String, Object> getProperties() {
        return this.properties;
    }

    @JsonAnySetter
    public void set(String key, String value) {
        if (this.properties == null) {
            this.properties = new HashMap<>();
        }
        this.properties.put(key, value);
    }

    @Transient
    public String get(String key) {
        if (this.properties == null || !this.properties.containsKey(key)){
            return null;
        } 
        return this.properties.get(key).toString();
    }

    public void setProperties(Map<String,Object> properties) {
        this.properties = properties;
    }

}
