package com.fantasy.wx.user.bean;

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

    public UserInfo(){}
    public UserInfo(String openid){this.openId=openid;}
    //用户的标识，对当前公众号唯一
    @Id
    @Column(name = "OPENID", nullable = false, insertable = true, updatable = false)
    private String openId;
    //用户的昵称
    @Column(name = "NICKNAME", length = 200)
    private String nickname;
    //用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
    @Column(name = "SEX")
    private String sex;
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
    private String headImgUrl;
    //用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间
    @Column(name = "SUBSCRIBE_TIME")
    private Long subscribeTime;
    //用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息。
    @Column(name = "SUBSCRIBE")
    private Boolean subscribe;
    @Column(name = "UNION_ID")
    private String unionId;
    //最后消息时间
    @Column(name = "LAST_MESSAGE_TIME")
    private Long lastMessageTime;
    //最后查看消息时间
    @Column(name = "LAST_LOOK_TIME")
    private Long lastLookTime;
    //未读消息条数
    @Column(name = "UN_READ_SIZE")
    private Integer unReadSize;

    public String getTime(){
        if(subscribeTime==0) return "";
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long time=subscribeTime*1000L;
        return sdf.format(new Date(time));
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
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

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public void setSubscribeTime(long subscribeTime) {
        this.subscribeTime = subscribeTime;
    }

    public boolean isSubscribe() {
        return subscribe;
    }

    public void setSubscribe(boolean subscribe) {
        this.subscribe = subscribe;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public Long getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(Long lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public Long getLastLookTime() {
        return lastLookTime;
    }

    public void setLastLookTime(Long lastLookTime) {
        this.lastLookTime = lastLookTime;
    }

    public Integer getUnReadSize() {
        return unReadSize;
    }

    public void setUnReadSize(Integer unReadSize) {
        this.unReadSize = unReadSize;
    }
}
