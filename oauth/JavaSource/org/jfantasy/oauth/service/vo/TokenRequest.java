package org.jfantasy.oauth.service.vo;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.jfantasy.oauth.bean.enums.GrantType;

public class TokenRequest {
    /**
     * 客户端的ID<br/>
     * 分配的 apiKey
     */
    @JsonProperty("client_id")
    private String apiKey;
    /**
     * 授权模式
     */
    @JsonProperty("grant_type")
    private GrantType grantType;
    /**
     * 授权码<br/>
     * 授权模式 为 授权码模式 时 必填
     */
    private String code;
    /**
     * 用户名<br/>
     * 授权模式 为 密码模式 时 必填
     */
    private String username;
    /**
     * 密码<br/>
     * 授权模式 为 密码模式 时 必填
     */
    private String password;
    /**
     * 表示权限范围，可选项。
     */
    private String scope;
    /**
     * 更新令牌<br/>
     * 授权模式 为 更新令牌模式 时 必填
     */
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
