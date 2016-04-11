package org.jfantasy.common.bean;

import org.jfantasy.framework.dao.BaseBusEntity;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 数据库连接配置
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-7-12 下午02:32:06
 */
@Entity
@Table(name = "JDBC_CONFIG")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class JdbcConfig extends BaseBusEntity {

    private static final long serialVersionUID = 7287559970566190214L;

    public enum DataBaseType {
        mySql("MYSQL数据库"), oracle("ORCL数据库"), sqlServer("SQL-SERVER数据库"), db2("DB2"), jndi("JNDI"), dataSource("DataSource");
        private String value;

        private DataBaseType(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    @Id
    @Column(name = "ID", insertable = true, updatable = false)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 数据库名称
     */
    @Column(name = "NAME", length = 150)
    private String name;
    /**
     * 数据库类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", nullable = false)
    private DataBaseType type;
    /**
     * 地址
     */
    @Column(name = "HOST_NAME", length = 50)
    private String hostname;
    /**
     * 端口
     */
    @Column(name = "port", length = 8)
    private Integer port;
    /**
     * 数据库名称
     */
    @Column(name = "DATABASE_NAME", length = 20)
    private String database;
    /**
     * 用户名
     */
    @Column(name = "USERNAME", length = 20)
    private String username;
    /**
     * 密码
     */
    @Column(name = "PASSWORD", length = 20)
    private String password;

    /**
     * 引用程序自身的datasource
     */
    @Column(name = "DATASOURCE_NAME", length = 20)
    private String dataSourceName;

    @Column(name = "JNDI_NAME", length = 20)
    private String jndiName;

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

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public DataBaseType getType() {
        return type;
    }

    public void setType(DataBaseType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getJndiName() {
        return jndiName;
    }

    public void setJndiName(String jndiName) {
        this.jndiName = jndiName;
    }
}
