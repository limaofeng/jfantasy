package org.jfantasy.oauth.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;
import java.util.List;

/**
 * 应用
 */
@ApiModel("应用配置")
@Entity
@Table(name = "OAUTH_APP")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Application extends BaseBusEntity {

    @Id
    @Column(name = "ID", updatable = false)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "DESCRIPTION")
    private String description;
    @OneToMany(mappedBy = "application", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    private List<ApiKey> apiKeys;
    @Column(name = "ENABLED",nullable = false)
    private Boolean enabled;

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

    public List<ApiKey> getApiKeys() {
        return apiKeys;
    }

    public void setApiKeys(List<ApiKey> apiKeys) {
        this.apiKeys = apiKeys;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
