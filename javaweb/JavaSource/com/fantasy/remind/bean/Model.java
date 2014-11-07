package com.fantasy.remind.bean;

import com.fantasy.file.bean.FileDetail;
import com.fantasy.framework.dao.BaseBusEntity;
import com.fantasy.framework.util.jackson.JSON;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.type.TypeReference;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

/**
 * 公告
 */

@Entity
@Table(name="remind_model")
public class Model extends BaseBusEntity {

    @Id
    @Column(name = "CODE", nullable = false, insertable = true, updatable = true, precision = 22, scale = 0)
    private String code;

    /**
     * 消息标题
     */
    @Column(name="TITLE",length = 500)
    private String title;

    /**
     * 图片存储
     */
    @Column(name = "MODEL_IMAGE_STORE", length = 3000)
    private String modelImageStore;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getModelImageStore() {
        if (StringUtils.isNotBlank(this.modelImageStore)) {
            List<FileDetail> fileDetails = JSON.deserialize(this.modelImageStore, new TypeReference<List<FileDetail>>() {
            });
            return fileDetails.isEmpty() ? null : fileDetails.get(0).getAbsolutePath();
        }
        return null;
    }
    @TypeConversion(key = "modelImageStore", converter = "com.fantasy.file.bean.converter.FileDetailStoreConverter")
    public void setModelImageStore(String modelImageStore) {
        this.modelImageStore = modelImageStore;
    }


}
