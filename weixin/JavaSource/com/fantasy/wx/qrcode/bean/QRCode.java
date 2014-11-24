package com.fantasy.wx.qrcode.bean;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by zzzhong on 2014/11/21.
 */
@Entity
@Table(name = "WX_QRCode")
public class QRCode {
    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = false, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 获取的二维码ticket，凭借此ticket可以在有效时间内换取二维码。
     */
    @Column(name = "TICKET")
    protected String ticket;
    @Column(name = "CREATE_TIME")
    protected Date createTime;
    /**
     * 二维码的有效时间，以秒为单位。最大不超过1800。
     */
    @Column(name = "EXPIRE_SECONDS")
    protected int expireSeconds = -1;
    /**
     * 二维码图片解析后的地址，开发者可根据该地址自行生成需要的二维码图片
     */
    @Column(name = "URL")
    protected String url;
    //关联的关键字
    @Column(name = "LINK_KEY")
    private String linkKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getExpireSeconds() {
        return expireSeconds;
    }

    public void setExpireSeconds(int expireSeconds) {
        this.expireSeconds = expireSeconds;
    }

    public String getLinkKey() {
        return linkKey;
    }

    public void setLinkKey(String linkKey) {
        this.linkKey = linkKey;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
