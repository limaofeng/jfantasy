package org.jfantasy.oauth;

import org.springframework.security.access.ConfigAttribute;

import java.io.Serializable;
import java.util.List;

public class PermissionRule implements Serializable {

    private String id;
    private String pattern;
    private String method;
    private List<ConfigAttribute> securityConfigs;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<ConfigAttribute> getSecurityConfigs() {
        return securityConfigs;
    }

    public void setSecurityConfigs(List<ConfigAttribute> securityConfigs) {
        this.securityConfigs = securityConfigs;
    }
}
