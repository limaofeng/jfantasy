package org.jfantasy.thirdparty.bean;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.thirdparty.bean.enums.Platform;

import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler", "properties"})
public class User extends BaseBusEntity {

    private static final long serialVersionUID = 4766200838709200330L;

    private Long id;

    private String key;

    private String nickname;
    /**
     * 平台
     */
    private Platform platform;

    private Map<String,Object> properties;
    /**
     * 关联的用户
     */
    private String withUser;

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

    public void setProperties(Map<String,Object> properties) {
        this.properties = properties;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public String getWithUser() {
        return withUser;
    }

    public void setWithUser(String withUser) {
        this.withUser = withUser;
    }

    @JsonAnyGetter
    public Map<String,Object> getProperties() {
        return this.properties;
    }

    @JsonAnySetter
    public void set(String key, Object value) {
        if (this.properties == null) {
            this.properties = new HashMap<>();
        }
        this.properties.put(key, value);
    }

}
