package com.fantasy.wx.bean;

import com.fantasy.framework.util.jackson.JSON;
import org.codehaus.jackson.type.TypeReference;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zzzhong on 2014/11/20.
 */
@Entity
@Table(name = "WX_GROUP_MESSAGE")
public class GroupMessage {
    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = false, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;

    /**
     * 消息类型
     */
    @Column(name = "MSG_TYPE", length = 50)
    private String msgType;
    /**
     * 文本消息内容
     */
    @Column(name = "CONTENT", length = 1000)
    private String content;
    /**
     * 媒体消息id
     */
    @Column(name = "MEDIA_ID", length = 100)
    private String mediaId;
    /**
     * 分组的id通过group api 获取
     */
    @Column(name = "GROUP_ID", length = 50)
    private Long groupId;
    /**
     * openid列表消息集合
     */
    @Column(name = "TO_USERS_DATA", length = 5000)
    private String toUsersData;

    @Transient
    private List<String> toUsers = new ArrayList<String>();

    /**
     * 最后修改时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "MODIFY_TIME")
    private Date modifyTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToUsersData() {
        return toUsersData;
    }

    public void setToUsersData(String toUsersData) {
        toUsers = JSON.deserialize(toUsersData, new TypeReference<List<String>>() {
        });
        this.toUsersData = toUsersData;
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

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public List<String> getToUsers() {
        return toUsers;
    }

    public void setToUsers(List<String> toUsers) {
        this.toUsers = toUsers;
        this.toUsersData = JSON.serialize(toUsers);
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

}
