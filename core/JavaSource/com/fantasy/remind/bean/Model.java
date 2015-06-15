package com.fantasy.remind.bean;

import com.fantasy.file.bean.FileDetail;
import com.fantasy.framework.dao.BaseBusEntity;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.framework.util.regexp.RegexpUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import java.util.List;

/**
 * 公告
 */

@Entity
@Table(name="remind_model")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "notices"})
public class Model extends BaseBusEntity {
    public Model(){}
    public Model(String code){
        this.code=code;
    }

    @Id
    @Column(name = "CODE", nullable = false, insertable = true, updatable = true, precision = 22, scale = 0)
    private String code;
    /**
     * 模版名称
     */
    @Column(name="NAME",length = 200)
    private String name;

    /**
     * 消息内容模版
     */
    @Column(name="CONTENT",length = 500)
    private String content;

    /**
     * 跳转连接模版
     */
    @Column(name="URL",length = 500)
    private String url;

    /**
     * 图片存储
     */
    @Column(name = "MODEL_IMAGE_STORE", length = 3000)
    private String modelImageStore;

    /**
     * 消息
     */
    @OneToMany(mappedBy = "model", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    private List<Notice> notices;

    @Transient
    private FileDetail avatar;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getModelImageStore() {
        return this.modelImageStore;
    }

    public void setModelImageStore(String modelImageStore) {
        this.modelImageStore = modelImageStore;
    }

    @Transient
    public void setModelImage(FileDetail fileDetail) {
        this.setModelImageStore(JSON.serialize(fileDetail));
    }

    @Transient
    public FileDetail getModelImage() {
        if (StringUtils.isEmpty(this.modelImageStore)) {
            return null;
        }
        return JSON.deserialize(this.modelImageStore, FileDetail.class);
    }

    public List<Notice> getNotices() {
        return notices;
    }

    public void setNotices(List<Notice> notices) {
        this.notices = notices;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAvatar() {
        if (this.avatar == null && StringUtils.isNotBlank(this.modelImageStore)) {
            List<FileDetail> fileDetails = JSON.deserialize(this.modelImageStore, new TypeReference<List<FileDetail>>() {
            });
            assert fileDetails != null;
            this.avatar = fileDetails.isEmpty() ? null : fileDetails.get(0);
        }
        if(this.avatar==null){
            return null;
        }
        return RegexpUtil.replace(this.avatar.getAbsolutePath(), "[.][a-zA-Z]{1,}$", "_250x153$0");
    }
}
