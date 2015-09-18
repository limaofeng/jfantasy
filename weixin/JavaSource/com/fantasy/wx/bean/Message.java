package com.fantasy.wx.bean;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;

/**
 * 文本消息
 * Created by zzzhong on 2014/6/17.
 */
@Entity(name="wxMessage")
@Table(name = "WX_MESSAGE")
public class Message {
    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = false, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    //表示是由客户端还是服务端发送的消息send客户端发送到服务端为client
    @Column(name = "TYPE")
    private String type = "client";

    @Column(name = "TO_USER_NAME")
    private String toUserName;

    @Transient
    private String fromUserName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OPENID",foreignKey = @ForeignKey(name = "FK_WX_MESSAGE_USERID"))
    private User user;

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        if (user == null)
            user = new User();
        user.setOpenId(fromUserName);
        this.fromUserName = fromUserName;
    }

    @Column(name = "CREATE_TIME")
    private Long createTime;

    @Column(name = "MSG_TYPE")
    private String msgType;



    @Column(name = "CONTENT", length = 1000)
    private String content;

    @Column(name = "MSG_ID")
    private Long msgId;

    @Column(name = "PIC_URL")
    private String picUrl;

    @Column(name = "MEDIAID")
    private String mediaId;

    //媒体文件路径数据 FileDetail的json数据
    @Column(name = "MEDIA_DATA",length=5000)
    private String mediaData;


    @Column(name = "FORMAT")
    private String format;

    @Column(name = "THUMB_MEDIAID")
    private String thumbMediaId;

    @Column(name = "LOCATION_X", precision = 15, scale = 5)
    private Double locationX;

    @Column(name = "LOCATION_Y", precision = 15, scale = 5)
    private Double locationY;

    @Column(name = "SCALE", precision = 15, scale = 5)
    private Double scale;

    @Column(name = "LABEL")
    private String label;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "URL")
    private String url;

    @Column(name = "EVENT")
    private String event;

    @Column(name = "EVENT_KEY")
    private String eventKey;

    @Column(name = "TICKET")
    private String ticket;

    @Column(name = "LATITUDE", precision = 15, scale = 5)
    private Double latitude;

    @Column(name = "LONGITUDE", precision = 15, scale = 5)
    private Double longitude;

    @Column(name = "PRECISION_VALUE", precision = 15, scale = 5)
    private Double precision;

    @Column(name = "RECOGNITION")
    private String recognition;

    ///////////////////////////////////////
    // 群发消息返回的结果
    ///////////////////////////////////////
    /**
     * 群发的结果
     */
    @Column(name = "STATUS")
    private String status;
    /**
     * group_id下粉丝数；或者openid_list中的粉丝数
     */
    @Column(name = "TOTAL_COUNT")
    private Integer totalCount;
    /**
     * 过滤（过滤是指特定地区、性别的过滤、用户设置拒收的过滤，用户接收已超4条的过滤）后，准备发送的粉丝数，原则上，filterCount = sentCount + errorCount
     */
    @Column(name = "FILTER_COUNT")
    private Integer filterCount;
    /**
     * 发送成功的粉丝数
     */
    @Column(name = "SENT_COUNT")
    private Integer sentCount;
    /**
     * 发送失败的粉丝数
     */
    @Column(name = "ERROR_COUNT")
    private Integer errorCount;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
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

    public String getThumbMediaId() {
        return thumbMediaId;
    }

    public void setThumbMediaId(String thumbMediaId) {
        this.thumbMediaId = thumbMediaId;
    }

    public Double getLocationX() {
        return locationX;
    }

    public void setLocationX(Double locationX) {
        this.locationX = locationX;
    }

    public Double getLocationY() {
        return locationY;
    }

    public void setLocationY(Double locationY) {
        this.locationY = locationY;
    }

    public Double getScale() {
        return scale;
    }

    public void setScale(Double scale) {
        this.scale = scale;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getPrecision() {
        return precision;
    }

    public void setPrecision(Double precision) {
        this.precision = precision;
    }

    public String getRecognition() {
        return recognition;
    }

    public void setRecognition(String recognition) {
        this.recognition = recognition;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getFilterCount() {
        return filterCount;
    }

    public void setFilterCount(Integer filterCount) {
        this.filterCount = filterCount;
    }

    public Integer getSentCount() {
        return sentCount;
    }

    public void setSentCount(Integer sentCount) {
        this.sentCount = sentCount;
    }

    public Integer getErrorCount() {
        return errorCount;
    }

    public String getMediaData() {
        return mediaData;
    }

    public void setMediaData(String mediaData) {
        this.mediaData = mediaData;
    }

    public void setErrorCount(Integer errorCount) {
        this.errorCount = errorCount;
    }

}
