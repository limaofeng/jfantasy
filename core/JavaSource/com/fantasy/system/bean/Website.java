package com.fantasy.system.bean;

import com.fantasy.file.bean.FileManagerConfig;
import com.fantasy.framework.dao.BaseBusEntity;
import com.fantasy.security.bean.Menu;
import com.fantasy.security.bean.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 站点配置表，一个应用维护多套网站时，需要对每个网站的基本信息进行一些配置
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2014-2-17 上午11:07:30
 */
@Entity
@Table(name = "SYS_WEBSITE")
@JsonIgnoreProperties(value = {"defaultFileManager", "defaultUploadFileManager", "settings", "users", "rootMenu"})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Website extends BaseBusEntity {

    private static final long serialVersionUID = 3763626581086219087L;

    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = false, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 网站key（唯一）
     */
    @Column(name = "CODE", length = 20, unique = true)
    private String key;
    /**
     * 网站名称
     */
    @Column(name = "NAME", length = 50)
    private String name;
    /**
     * 网址
     */
    @Column(name = "WEB", length = 100)
    private String web;
    /**
     * 网站对应的默认文件管理器(发布文章及日志)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEFAULT_FILEMANAGER", nullable = false, foreignKey = @ForeignKey(name = "FK_WEBSITE_DEF_FMID"))
    private FileManagerConfig defaultFileManager;
    /**
     * 网站对应的默认上传文件管理器(发布文章及日志)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEFAULT_UPLOAD_FILEMANAGER", nullable = false, foreignKey = @ForeignKey(name = "FK_WEBSITE_DEF_UPLOAD_FMID"))
    private FileManagerConfig defaultUploadFileManager;
    /**
     * 菜单根
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "MENU_ID", foreignKey = @ForeignKey(name = "FK_WEBSITE_MENU_PID"))
    private Menu rootMenu;
    /**
     * 网站设置
     */
    @OneToMany(mappedBy = "website", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Setting> settings = new ArrayList<Setting>();
    /**
     * 网站用户信息
     */
    @OneToMany(mappedBy = "website", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<User> users;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public FileManagerConfig getDefaultFileManager() {
        return defaultFileManager;
    }

    public void setDefaultFileManager(FileManagerConfig defaultFileManager) {
        this.defaultFileManager = defaultFileManager;
    }

    public List<Setting> getSettings() {
        return settings;
    }

    public void setSettings(List<Setting> settings) {
        this.settings = settings;
    }

    public FileManagerConfig getDefaultUploadFileManager() {
        return defaultUploadFileManager;
    }

    public void setDefaultUploadFileManager(FileManagerConfig defaultUploadFileManager) {
        this.defaultUploadFileManager = defaultUploadFileManager;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Menu getRootMenu() {
        return rootMenu;
    }

    public void setRootMenu(Menu rootMenu) {
        this.rootMenu = rootMenu;
    }
}
