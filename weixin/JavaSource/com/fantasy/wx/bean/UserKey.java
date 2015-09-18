package com.fantasy.wx.bean;


import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.persistence.Column;
import java.io.Serializable;

public class UserKey implements Serializable {

    public static UserKey newInstance(String absolutePath, String fileManagerId) {
        return new UserKey(absolutePath, fileManagerId);
    }

    //APPID
    @Column(name = "APPID", unique = true, updatable = false)
    private String appId;
    //用户的标识，对当前公众号唯一
    @Column(name = "OPENID", unique = true, updatable = false)
    private String openId;

    public UserKey() {
    }

    public UserKey(String appId, String openId) {
        super();
        this.appId = appId;
        this.openId = openId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().appendSuper(super.hashCode()).append(this.getAppId()).append(this.getOpenId()).toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof UserKey) {
            UserKey key = (UserKey) o;
            return new EqualsBuilder().appendSuper(super.equals(o)).append(this.getAppId(), key.getAppId()).append(this.getOpenId(), key.getOpenId()).isEquals();
        }
        return false;
    }

    @Override
    public String toString() {
        return this.getAppId() + ":" + this.getOpenId();
    }

}
