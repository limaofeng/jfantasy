package com.fantasy.wx.bean.pojo;

import com.fantasy.framework.dao.BaseBusEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 微信通用接口凭证
 * Created by zzzhong on 2014/6/18.
 */
@Entity
@Table(name = "WX_ACCESS_TOKEN")
public class AccessToken extends BaseBusEntity{
    public AccessToken(){}
    public AccessToken(String appid,String appsecret,String tokenName){
        this.appid=appid;
        this.appsecret=appsecret;
        this.tokenName=tokenName;
    }
    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = false, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;

    //开发者凭证
    @Column(name = "APP_ID", length = 200)
    private String appid;
    @Column(name = "APP_SECRET", length = 200)
    private String appsecret;

    //微信服务器配置的token
    @Column(name = "TOKEN_NAME", length =200)
    private String tokenName;
    // 获取到的凭证
    @Column(name = "TOKEN", length = 500)
    private String token;
    // 凭证有效时间，单位：秒
    @Column(name = "EXPIRES_IN")
    private Integer expiresIn;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    public Integer getExpiresIn() {
        return expiresIn;
    }
    public String getExpiresInTime() {
        if(expiresIn==null) return "";
        SimpleDateFormat  sdf=new SimpleDateFormat("hh:mm:ss");
        return sdf.format(new Date(expiresIn*1000L));
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAppsecret() {
        return appsecret;
    }

    public void setAppsecret(String appsecret) {
        this.appsecret = appsecret;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
