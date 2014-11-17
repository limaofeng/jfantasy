package com.fantasy.wx.bean.pojo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Map;

/**
 * Created by zzzhong on 2014/9/4.
 */
@Entity
@Table(name = "WX_CODE")
public class Code {
    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = true, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;

    /**
     * 二维码类型，QR_SCENE为临时,QR_LIMIT_SCENE为永久
     */
    @Column(name = "ACTION_NAME", length = 50)
    private String actionName;
    @Column(name = "ACTION_INFO")
    private String actionInfo;

    /**
     * 二维码详细信息
     */
    @Transient
    private Map<String,Object> actionInfoMap;

    /**
     * 二维码的有效时间，以秒为单位。最大不超过1800。
     */
    @Column(name = "EXPIRE_SECONDS")
    private Integer expireSeconds;

    /**
     * 获取的二维码ticket，凭借此ticket可以在有效时间内换取二维码。
     */
    @Column(name = "TICKET", length = 500)
    private String ticket;

    /**
     * 二维码图片解析后的地址，开发者可根据该地址自行生成需要的二维码图片
     */
    @Column(name = "URL", length = 500)
    private String url;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getActionInfo() {
        return actionInfo;
    }

    public void setActionInfo(String actionInfo) {
        this.actionInfo = actionInfo;
    }

    public Map<String, Object> getActionInfoMap() {
        return actionInfoMap;
    }

    public void setActionInfoMap(Map<String, Object> actionInfoMap) {
        this.actionInfoMap = actionInfoMap;
    }

    public Integer getExpireSeconds() {
        return expireSeconds;
    }

    public void setExpireSeconds(Integer expireSeconds) {
        this.expireSeconds = expireSeconds;
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
}
