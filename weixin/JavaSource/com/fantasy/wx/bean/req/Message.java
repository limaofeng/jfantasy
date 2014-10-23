package com.fantasy.wx.bean.req;

import com.fantasy.wx.bean.pojo.UserInfo;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 文本消息
 * Created by zzzhong on 2014/6/17.
 */
@Entity
@Table(name = "WX_MESSAGE")
public class Message {
    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = false, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    //消息类型
    @Column(name = "TYPE")
    private String type;

    // 开发者微信号
    @Column(name = "TO_USER_NAME")
    private String toUserName;

    //发送消息的用户openid
    private String fromUserName;

    // 消息创建时间 （整型）
    @Column(name = "CREATE_TIME")
    private long createTime;
    // 消息类型（text/image/location/link）
    @Column(name = "MSG_TYPE")
    private String msgType;
    // 消息id，64位整型
    @Column(name = "MSG_ID")
    private long msgId;

    // 消息内容
    @Column(name = "CONTENT",length=5000)
    private String content;


    //事件类型
    @Column(name = "EVENT")
    private String event;
    //事件key
    @Column(name = "EVENT_KEY")
    private String eventKey;


    // 消息标题
    @Column(name = "TITLE",length=500)
    private String title;
    // 消息描述
    @Column(name = "DESCRIPTION",length=2000)
    private String description;
    // 消息链接
    @Column(name = "URL",length=500)
    private String url;


    // 地理位置维度
    @Column(name = "LOCATION_X")
    private String locationX;
    // 地理位置经度
    @Column(name = "LOCATION_Y")
    private String locationY;
    // 地图缩放大小
    @Column(name = "SCALE")
    private String scale;
    // 地理位置信息
    @Column(name = "LABEL")
    private String label;

    // 图片链接
    @Column(name = "PIC_URL")
    private String picUrl;


    // 媒体ID
    @Column(name = "MEDIAID")
    private String mediaId;
    // 语音格式
    @Column(name = "FORMAT")
    private String format;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OPENID")
    private UserInfo userInfo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        if(userInfo==null){
            userInfo=new UserInfo();
        }
        userInfo.setOpenid(fromUserName);
        this.fromUserName = fromUserName;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLocationX() {
        return locationX;
    }

    public void setLocationX(String locationX) {
        this.locationX = locationX;
    }

    public String getLocationY() {
        return locationY;
    }

    public void setLocationY(String locationY) {
        this.locationY = locationY;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
