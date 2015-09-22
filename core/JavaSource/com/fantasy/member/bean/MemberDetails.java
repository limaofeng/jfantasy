package com.fantasy.member.bean;

import com.fantasy.file.bean.FileDetail;
import com.fantasy.file.bean.converter.FileDetailConverter;
import com.fantasy.file.bean.databind.FileDetailDeserializer;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.security.bean.enums.Sex;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户详细信息表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-3-25 下午03:43:54
 */
@ApiModel("会员详细信息")
@Entity
@JsonFilter(JSON.CUSTOM_FILTER)
@Table(name = "MEM_MEMBER_DETAILS")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "member", "avatarStore","avatar"})
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
    @ApiModelProperty("姓名")
    @Column(name = "NAME", length = 20)
    private String name;
    /**
     * 性别
     */
    @ApiModelProperty("性别")
    @Enumerated(EnumType.STRING)
    @Column(name = "SEX", length = 20)
    private Sex sex;
    /**
     * 生日
     */
    @ApiModelProperty("生日")
    @Column(name = "BIRTHDAY")
    private Date birthday;
    /**
     * 移动电话
     */
    @ApiModelProperty("移动电话")
    @Column(name = "MOBILE", length = 20)
    private String mobile;
    /**
     * 固定电话
     */
    @ApiModelProperty("固定电话")
    @Column(name = "TEL", length = 20)
    private String tel;
    /**
     * E-mail
     */
    @ApiModelProperty("E-mail")
    @Column(name = "EMAIL", length = 50)
    private String email;
    /**
     * 邮箱是否验证
     */
    @ApiModelProperty("邮箱是否验证")
    @Column(name = "MAIL_VALID", nullable = false)
    private Boolean mailValid;
    /**
     * 手机号是否验证
     */
    @ApiModelProperty("手机号是否验证")
    @Column(name = "MOBILE_VALID", nullable = false)
    private Boolean mobileValid;
    /**
     * 网址
     */
    @ApiModelProperty("网址")
    @Column(name = "WEBSITE", length = 50)
    private String website;
    /**
     * 描述信息
     */
    @ApiModelProperty("描述信息")
    @Column(name = "DESCRIPTION")
    private String description;
    /**
     * 用户积分
     */
    @ApiModelProperty("积分")
    @Column(name = "score", length = 10)
    private Integer score;
    /**
     * 用户头像存储
     */
    @ApiModelProperty(hidden = true)
    @Column(name = "AVATAR", length = 500)
    @Convert(converter = FileDetailConverter.class)
    private FileDetail avatar;

    @ApiModelProperty(hidden = true)
    @OneToOne(fetch = FetchType.LAZY, targetEntity = Member.class, mappedBy = "details")
    private Member member;

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

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

    @JsonDeserialize(using = FileDetailDeserializer.class)
    public void setAvatar(FileDetail fileDetail) {
        this.avatar = fileDetail;
    }

    public FileDetail getAvatar() {
        return this.avatar;
    }

}
