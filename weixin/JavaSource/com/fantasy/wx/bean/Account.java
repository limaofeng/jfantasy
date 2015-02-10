package com.fantasy.wx.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import com.fantasy.wx.framework.session.AccountDetails;

import javax.persistence.*;

/**
 * 微信公众号设置
 * Created by zzzhong on 2014/6/18.
 */
@Entity
@Table(name = "WX_ACCOUNT")
public class Account extends BaseBusEntity implements AccountDetails {

    //开发者凭证
    @Id
    @Column(name = "APP_ID", length = 200)
    private String appId;
    /**
     * 原始ID
     */
    @Column(name = "PRIMITIVE_ID", length = 200)
    private String primitiveId;

    @Column(name = "APP_SECRET", length = 200)
    private String secret;
    /**
     * 公众号类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", length = 20)
    private Type type;
    /**
     * 公众号名称
     */
    @Column(name = "NAME", length = 100)
    public String name;
    //微信服务器配置的token
    @Column(name = "TOKEN_NAME", length = 200)
    private String token;

    @Column(name = "AES_KEY", length = 200)
    private String aesKey;

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
    }

    @Override
    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String getAesKey() {
        return this.aesKey;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String getAppId() {
        return this.appId;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getPrimitiveId() {
        return primitiveId;
    }

    public void setPrimitiveId(String primitiveId) {
        this.primitiveId = primitiveId;
    }
}
