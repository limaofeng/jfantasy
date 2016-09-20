package org.jfantasy.auth.rest.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.jfantasy.framework.spring.validation.RESTful;
import org.jfantasy.member.service.vo.AuthType;

import javax.validation.constraints.NotNull;

public class LoginForm {
    /**
     * 登录方式
     */
    private AuthType type = AuthType.password;
    /**
     * 用户类型
     */
    @JsonProperty("user_type")
    private String userType;
    /**
     * 用户名
     */
    @NotNull(groups = RESTful.POST.class)
    private String username;
    /**
     * 范围
     */
    @NotNull(groups = RESTful.POST.class)
    private Scope scope;
    /**
     * type=password时为登陆密码,type=authcode时为短信验证码
     */
    @NotNull(groups = RESTful.POST.class)
    private String password;

    public AuthType getType() {
        return type;
    }

    public void setType(AuthType type) {
        this.type = type;
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

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }
}
