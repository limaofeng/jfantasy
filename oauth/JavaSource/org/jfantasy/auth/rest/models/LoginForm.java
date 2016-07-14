package org.jfantasy.auth.rest.models;


import io.swagger.annotations.ApiModelProperty;

public class LoginForm {
    @ApiModelProperty("登录方式")
    private LoginType type = LoginType.password;
    @ApiModelProperty("用户类型")
    private String userType;
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("范围")
    private Scope scope;
    @ApiModelProperty("type=password时为登陆密码,type=authcode时为短信验证码")
    private String password;

    public LoginType getType() {
        return type;
    }

    public void setType(LoginType type) {
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
