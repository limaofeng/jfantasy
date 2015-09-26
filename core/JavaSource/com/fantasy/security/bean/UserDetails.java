package com.fantasy.security.bean;

import com.fantasy.file.bean.FileDetail;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.security.bean.enums.Sex;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.StringUtils;
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
@ApiModel(value = "用户详细信息", description = "用户详细信息")
@Entity
@Table(name = "AUTH_USER_DETAILS")
@JsonFilter(JSON.CUSTOM_FILTER)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "user", "avatarStore"})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserDetails implements Serializable {

    private static final long serialVersionUID = -5738290484268799275L;

    @ApiModelProperty(value = "用户ID")
    @Id
    @Column(name = "USER_ID", nullable = false, insertable = true, updatable = false, precision = 22, scale = 0)
    @GenericGenerator(name = "pkGenerator", strategy = "foreign", parameters = {@Parameter(name = "property", value = "user")})
    @GeneratedValue(generator = "pkGenerator")
    private Long userId;

    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    @Column(name = "NAME", length = 20)
    private String name;
    /**
     * 性别
     */
    @ApiModelProperty(value = "性别")
    @Enumerated(EnumType.STRING)
    @Column(name = "SEX", length = 20)
    private Sex sex;
    /**
     * 生日
     */
    @ApiModelProperty(value = "生日")
    @Column(name = "BIRTHDAY")
    private Date birthday;
    /**
     * 移动电话
     */
    @ApiModelProperty(value = "移动电话")
    @Column(name = "MOBILE", length = 20)
    private String mobile;
    /**
     * 固定电话
     */
    @ApiModelProperty(value = "固定电话")
    @Column(name = "TEL", length = 20)
    private String tel;
    /**
     * E-mail
     */
    @ApiModelProperty(value = "E-mail")
    @Column(name = "EMAIL", length = 50)
    private String email;
    /**
     * 网址
     */
    @ApiModelProperty(value = "网址")
    @Column(name = "WEBSITE", length = 50)
    private String website;
    /**
     * 描述信息
     */
    @ApiModelProperty(value = "描述")
    @Column(name = "DESCRIPTION")
    private String description;
    /**
     * 用户头像存储
     */
    @ApiModelProperty(hidden = true)
    @Column(name = "AVATAR", length = 500)
    private String avatarStore;

    @ApiModelProperty(hidden = true)
    @OneToOne(fetch = FetchType.LAZY, targetEntity = User.class, mappedBy = "details")
    private User user;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAvatarStore() {
        return avatarStore;
    }

    public void setAvatarStore(String avatarStore) {
        this.avatarStore = avatarStore;
    }

    @Transient
    public void setAvatar(FileDetail fileDetail) {
        this.setAvatarStore(JSON.serialize(fileDetail));
    }

    @ApiModelProperty(value = "用户头像")
    @Transient
    public FileDetail getAvatar() {
        if (StringUtils.isEmpty(this.avatarStore)) {
            return null;
        }
        return JSON.deserialize(this.avatarStore, FileDetail.class);
    }

}
