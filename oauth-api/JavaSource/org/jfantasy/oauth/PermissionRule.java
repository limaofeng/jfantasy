package org.jfantasy.oauth;

import org.jfantasy.framework.util.common.StringUtil;

import java.io.Serializable;

public class PermissionRule implements Serializable {

    private String id;
    private String pattern;
    private String method;
    private String access = "";

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

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public void addAccess(String access) {
        this.access += ((StringUtil.isBlank(this.access) ? "" : ",") + access.toUpperCase());
    }

}
