package com.fantasy.remind.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 提醒
 */

@Entity
@Table(name="remind_notice")
public class Notice extends BaseBusEntity {

    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = true, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 提醒内容
     */
    @Column(name="CONTENT",length = 500)
    private String content;


    /**
     * 跳转连接
     */
    @Column(name="URL")
    private String url;

    /**
     * 是否已读
     */
    @Column(name="IS_READ")
    private Boolean isRead=false;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CODE", nullable = false, foreignKey = @ForeignKey(name = "FK_NOTICE_MODEL_CODE"))
    private Model model;

    @Transient
    private String replaceMap;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public String getReplaceMap() {
        return replaceMap;
    }

    public void setReplaceMap(String replaceMap) {
        this.replaceMap = replaceMap;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }
}
