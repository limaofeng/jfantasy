package com.fantasy.member.bean;

import com.fantasy.file.bean.FileDetail;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.security.bean.enums.Sex;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;
import org.apache.commons.lang.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户详细信息表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-3-25 下午03:43:54
 */
@Entity
@Table(name = "MEM_MEMBER_DETAILS")
@JsonIgnoreProperties({"hibernateLazyInitializer", "member", "avatarStore"})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MemberDetails implements Serializable {

    private static final long serialVersionUID = -5738290484268799275L;

    @Id
    @Column(name = "MEMBER_ID", nullable = false, insertable = true, updatable = false, precision = 22, scale = 0)
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


    @Column(name = "MAIL_VALID", nullable = false)
    private Boolean mailValid;

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
     * 是否为vip用户
     */
    @Column(name = "VIP")
    private Boolean vip;
    /**
     * 用户积分
     */
    @Column(name = "score", length = 10)
    private Integer score;
    /**
     * 用户头像存储
     */
    @Column(name = "AVATAR", length = 500)
    private String avatarStore;

    @Transient
    private FileDetail avatar;

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Boolean getVip() {
        return vip;
    }

    public void setVip(Boolean vip) {
        this.vip = vip;
    }

    @OneToOne(fetch = FetchType.LAZY, targetEntity = Member.class, mappedBy = "details")
    private Member member;

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

    public String getAvatarStore() {
        return this.avatarStore;
    }

    @TypeConversion(key = "avatarStore", converter = "com.fantasy.file.bean.converter.FileDetailStoreConverter")
    public void setAvatarStore(String avatarStore) {
        this.avatarStore = avatarStore;
    }

    public FileDetail getAvatar() {
        if (this.avatar == null && StringUtils.isNotBlank(this.avatarStore)) {
            List<FileDetail> fileDetails = JSON.deserialize(this.avatarStore, new TypeReference<List<FileDetail>>() {
            });
            this.avatar = fileDetails.isEmpty() ? null : fileDetails.get(0);
        }
        return this.avatar;
    }

}
