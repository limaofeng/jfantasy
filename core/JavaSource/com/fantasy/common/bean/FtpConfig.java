package com.fantasy.common.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Ftp 连接配置
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-7-12 下午02:32:45
 */
@Entity
@Table(name = "FTP_CONFIG")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FtpConfig extends BaseBusEntity {

    private static final long serialVersionUID = 5513428236803813302L;

    @Id
    @Column(name = "ID", insertable = true, updatable = false)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 名称
     */
    @Column(name = "NAME", length = 150)
    private String name;
    /**
     * 端口
     */
    @Column(name = "port", length = 8)
    private Integer port = 21;
    /**
     * FTP服务地址
     */
    @Column(name = "HOST_NAME", length = 50)
    private String hostname;
    /**
     * 登陆名称
     */
    @Column(name = "USERNAME", length = 20)
    private String username;
    /**
     * 登陆密码
     */
    @Column(name = "PASSWORD", length = 20)
    private String password;
    /**
     * 编码格式
     */
    @Column(name = "CONTROL_ENCODING", length = 20)
    private String controlEncoding;
    /**
     * 默认目录
     */
    @Column(name = "DEFAULT_DIR", length = 150)
    private String defaultDir;
    /**
     * 描述
     */
    @Column(name = "DESCRIPTION", length = 250)
    private String description;

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDefaultDir() {
        return defaultDir;
    }

    public void setDefaultDir(String defaultDir) {
        this.defaultDir = defaultDir;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getControlEncoding() {
        return controlEncoding;
    }

    public void setControlEncoding(String controlEncoding) {
        this.controlEncoding = controlEncoding;
    }

}
