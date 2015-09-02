package com.fantasy.security.bean;

import com.fantasy.security.bean.enums.PermissionType;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class PermissionRule {
    /**
     * 规则类型
     */
    private PermissionType type;
    /**
     * 访问方式
     */
    private HttpMethod httpMethod;
    /**
     * 规则表达式
     */
    private String pattern;

    public PermissionType getType() {
        return type;
    }

    public void setType(PermissionType type) {
        this.type = type;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public RequestMatcher getRequestMatcher(){
        return null;
    }

}
