package org.jfantasy.oauth.bean;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

/**
 * API 授权
 */
@Entity
@Table(name = "OAUTH_APIKEY")
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler", "application"})
public class ApiKey extends BaseBusEntity {

    private static final long serialVersionUID = 1169698154678310660L;

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
    @Column(name = "PROPERTIES", columnDefinition = "Blob")
    private Map<String,Object> properties;
    /**
     * 对应的应用
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "APP_ID", updatable = false, foreignKey = @ForeignKey(name = "FK_OAUTH_APPKEY_APPID"))
    private Application application;
    /**
     * 平台简码(web/app)
     */
    @Column(name = "PLATFORM")
    private String platform;

    public ApiKey() {
    }

    public ApiKey(String key) {
        this();
        this.setKey(key);
    }

    @JsonAnyGetter
    public Map<String,Object> getProperties() {
        return properties;
    }

    @JsonAnySetter
    public void set(String key, Object value) {
        if (this.properties == null) {
            this.properties = new HashMap();
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

    public void setProperties(Map<String,Object> properties) {
        this.properties = properties;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    @Override
    public String toString() {
        return "ApiKey{" +
                "key='" + key + '\'' +
                ", application=" + application +
                ", platform='" + platform + '\'' +
                '}';
    }
}
