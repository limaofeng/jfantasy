package org.jfantasy.file.bean;

import org.jfantasy.file.bean.converter.ConfigParamsConverter;
import org.jfantasy.file.bean.enums.FileManagerType;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.jackson.JSON;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件管理器配置表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-7-12 下午02:30:29
 */
@Entity
@Table(name = "FILE_MANAGER_CONFIG")
@JsonFilter(JSON.CUSTOM_FILTER)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "folders", "fileDetails"})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FileManagerConfig extends BaseBusEntity {

    private static final long serialVersionUID = -4833473939396674528L;

    public FileManagerConfig() {
    }

    public FileManagerConfig(String id) {
        this.id = id;
    }

    /**
     * 唯一别名，作为文件管理器的ID
     */
    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = false, length = 50)
    private String id;
    /**
     * 文件管理器名称
     */
    @Column(name = "NAME", length = 150, nullable = false)
    private String name;
    /**
     * 文件管理器的类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", length = 20, nullable = false, insertable = true, updatable = false)
    private FileManagerType type;
    /**
     * 描述
     */
    @Column(name = "DESCRIPTION", length = 250)
    private String description;
    /**
     * 存放配置参数
     */
    @Column(name = "CONFIG_PARAM_STORE", length = 500)
    @Convert(converter = ConfigParamsConverter.class)
    private List<ConfigParam> configParams;
    /**
     * 文件管理器对应的目录
     */
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "FILE_MANAGER_CONFIG_ID")
    private List<Folder> folders;

    /**
     * 文件管理器对应的文件
     */
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "FILE_MANAGER_CONFIG_ID")
    private List<FileDetail> fileDetails;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FileManagerType getType() {
        return type;
    }

    public void setType(FileManagerType type) {
        this.type = type;
    }

    public List<Folder> getFolders() {
        return folders;
    }

    public void setFolders(List<Folder> folders) {
        this.folders = folders;
    }

    public List<FileDetail> getFileDetails() {
        return fileDetails;
    }

    public void setFileDetails(List<FileDetail> fileDetails) {
        this.fileDetails = fileDetails;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ConfigParam> getConfigParams() {
        return this.configParams;
    }

    public void setConfigParams(List<ConfigParam> configParams) {
        this.configParams = configParams;
    }

    public void addConfigParam(String name, String value) {
        if (this.configParams == null) {
            this.configParams = new ArrayList<ConfigParam>();
        }
        ConfigParam configParam = ObjectUtil.find(this.configParams, "name", name);
        if (configParam == null) {
            this.configParams.add(new ConfigParam(name, value));
        } else {
            configParam.setValue(value);
        }
    }

    @Override
    public String toString() {
        return "FileManagerConfig{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", description='" + description + '\'' +
                ", configParams=" + configParams +
                '}';
    }
}
