package org.jfantasy.oauth.bean;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;
import java.util.Properties;

/**
 * API 授权
 */
@ApiModel("API 授权信息")
@Entity
@Table(name = "OAUTH_APIKEY")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "application"})
public class ApiKey extends BaseBusEntity {
    /**
     * 调用 api key
     */
    @Id
    @Column(name = "_KEY", updatable = false)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String key;
    /**
     * 描述
     */
    @Column(name = "DESCRIPTION")
    private String description;
    /**
     * api 配置的一些额外信息
     */
    @ApiModelProperty(hidden = true)
    @Column(name = "PROPERTIES", columnDefinition = "Blob")
    private Properties properties;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "APP_ID", updatable = false, foreignKey = @ForeignKey(name = "FK_OAUTH_APPKEY_APPID"))
    private Application application;

    public ApiKey() {
    }

    public ApiKey(String key) {
        this();
        this.setKey(key);
    }

    @JsonAnyGetter
    public Properties getProperties() {
        return properties;
    }

    @JsonAnySetter
    public void set(String key, Object value) {
        if (this.properties == null) {
            this.properties = new Properties();
        }
        this.properties.put(key, value);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }
}
