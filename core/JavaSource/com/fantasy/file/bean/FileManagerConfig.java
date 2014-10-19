package com.fantasy.file.bean;

import com.fantasy.common.bean.FtpConfig;
import com.fantasy.common.bean.JdbcConfig;
import com.fantasy.file.bean.enums.FileManagerType;
import com.fantasy.framework.dao.BaseBusEntity;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import javax.persistence.*;
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
@JsonIgnoreProperties({"hibernateLazyInitializer", "folders", "fileDetails"})
public class FileManagerConfig extends BaseBusEntity {

    private static final long serialVersionUID = -4833473939396674528L;

    /**
     * 唯一别名，作为文件管理器的ID
     */
    @Id
    @Column(name = "BEAN_NAME", nullable = false, insertable = true, updatable = false, length = 50)
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

    /*------------------------------ FTP 配置属性-------------------------------*/
    @JoinColumn(name = "FTP_CONFIG_ID",foreignKey = @ForeignKey(name = "FK_FILE_MANAGER_FTP_CONFIG") )
    @ManyToOne(fetch = FetchType.LAZY)

    private FtpConfig ftpConfig;

    /*------------------------------ JDBC 配置属性-------------------------------*/
    @JoinColumn(name = "JDBC_CONFIG_ID",foreignKey =@ForeignKey(name = "FK_FILE_MANAGER_JDBC_CONFIG"))
    @ManyToOne(fetch = FetchType.LAZY)

    private JdbcConfig jdbcConfig;

    /*------------------------------- Local 配置属性 -----------------------------*/
    @Column(name = "LOCAL_DEFAULT_DIR", length = 350)
    private String localDefaultDir;

    /*------------------------------- 虚拟目录 配置属性 -----------------------------*/
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "SOURCE_ID")
    private FileManagerConfig source;

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

    public FtpConfig getFtpConfig() {
        return ftpConfig;
    }

    public void setFtpConfig(FtpConfig ftpConfig) {
        this.ftpConfig = ftpConfig;
    }

    public JdbcConfig getJdbcConfig() {
        return jdbcConfig;
    }

    public void setJdbcConfig(JdbcConfig jdbcConfig) {
        this.jdbcConfig = jdbcConfig;
    }

    public String getLocalDefaultDir() {
        return localDefaultDir;
    }

    public void setLocalDefaultDir(String localDefaultDir) {
        this.localDefaultDir = localDefaultDir;
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

    public FileManagerConfig getSource() {
        return source;
    }

    public void setSource(FileManagerConfig source) {
        this.source = source;
    }

    public static FileManagerConfig newInstance(String key, String name, String localDefaultDir, String description) {
        FileManagerConfig config = new FileManagerConfig();
        config.setId(key);
        config.setName(name);
        config.setType(FileManagerType.local);
        config.setLocalDefaultDir(localDefaultDir);
        config.setDescription(description);
        return config;
    }

    public static FileManagerConfig newInstance(String key) {
        FileManagerConfig config = new FileManagerConfig();
        config.setId(key);
        return config;
    }

    public static FileManagerConfig newInstance(String key, String name, FileManagerConfig source, String description) {
        FileManagerConfig config = new FileManagerConfig();
        config.setId(key);
        config.setName(name);
        config.setType(FileManagerType.virtual);
        config.setSource(source);
        config.setDescription(description);
        return config;
    }
}
