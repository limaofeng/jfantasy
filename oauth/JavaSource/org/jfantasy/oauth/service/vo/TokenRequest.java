package org.jfantasy.oauth.service.vo;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.jfantasy.oauth.bean.enums.GrantType;

public class TokenRequest {
    @ApiModelProperty(name = "client_id", value = "客户端的ID", notes = "分配的 apiKey", required = true)
    @JsonProperty("client_id")
    private String apiKey;
    @ApiModelProperty(name = "grant_type", value = "授权模式", required = true)
    @JsonProperty("grant_type")
    private GrantType grantType;
    @ApiModelProperty(value = "授权码", notes = "授权模式 为 授权码模式 时 必填")
    private String code;
    @ApiModelProperty(value = "用户名", notes = "授权模式 为 密码模式 时 必填")
    private String username;
    @ApiModelProperty(value = "密码", notes = "授权模式 为 密码模式 时 必填")
    private String password;
    @ApiModelProperty("表示权限范围，可选项。")
    private String scope;
    @ApiModelProperty(name = "refresh_token",value = "更新令牌", notes = "授权模式 为 更新令牌模式 时 必填")
    @JsonProperty("refresh_token")
    private String refreshToken;

    public GrantType getGrantType() {
        return grantType;
    }

    public void setGrantType(GrantType grantType) {
        this.grantType = grantType;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
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

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
