package com.fantasy.wx.bean.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 微信用户基本信息
 * Created by zzzhong on 2014/6/19.
 */
@Entity
@Table(name = "WX_USER_INFO")
public class UserInfo {
    //用户的标识，对当前公众号唯一
    @Id
    @Column(name = "OPENID", nullable = false, insertable = true, updatable = false)
    private String openid;
    //用户的昵称
    @Column(name = "NICKNAME", length = 200)
    private String nickname;
    //用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
    @Column(name = "SEX")
    private int sex;
    //用户所在城市
    @Column(name = "CITY")
    private String city;
    //用户所在国家
    @Column(name = "COUNTRY")
    private String country;
    //用户所在省份
    @Column(name = "PROVINCE")
    private String province;
    //用户的语言，简体中文为zh_CN
    @Column(name = "LANGUAGE")
    private String language;
    //用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空
    @Column(name = "HEADIMGURL" ,length = 600)
    private String headimgurl;
    //用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间
    @Column(name = "SUBSCRIBE_TIME")
    private Long subscribe_time;
    //用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息。
    @Column(name = "SUBSCRIBE")
    private int subscribe;
    //备注
    @Column(name = "remark")
    private String remark;
    //状态
    @Column(name = "state")
    private String state;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public Long getSubscribe_time() {
        return subscribe_time;
    }

    public void setSubscribe_time(Long subscribe_time) {
        this.subscribe_time = subscribe_time;
    }
    public String getTime(){
        if(subscribe_time==null) return "";
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long time=subscribe_time*1000L;
        return sdf.format(new Date(time));
    }

    public int getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(int subscribe) {
        this.subscribe = subscribe;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
