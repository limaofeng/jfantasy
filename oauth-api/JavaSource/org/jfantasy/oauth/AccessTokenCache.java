package org.jfantasy.oauth;

import org.jfantasy.framework.autoconfigure.HttpOAuthSettings;

import java.util.Date;

/**
 * 用于处理应用直接的授权问题
 */
public class AccessTokenCache {
    /**
     * app name
     */
    private String appName;
    /**
     * app Key
     */
    private String appKey;
    /**
     * app description
     */
    private String appDescription;

    /**
     * 授权模式
     */
    private String grantType;
    /**
     * 类型
     */
    private String type;
    /**
     * 访问令牌的有效时间，单位是秒。
     */
    private long expires;
    /**
     * 权限范围
     */
    private String scope;
    /**
     * 更新令牌
     */
    private String refreshToken;
    /**
     * 刷新令牌的有效时间，单位是秒。
     */
    private long reExpires;
    /**
     * 描述
     */
    private String description;
    /**
     * token 对应的 api key
     */
    private String apiKey;
    /**
     * token 对应的 app id
     */
    private Long appId;
    /**
     * token 的创建时间
     */
    private Date tokenCreationTime;

    public AccessTokenCache(HttpOAuthSettings httpSettings) {

    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppDescription() {
        return appDescription;
    }

    public void setAppDescription(String appDescription) {
        this.appDescription = appDescription;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getExpires() {
        return expires;
    }

    public void setExpires(long expires) {
        this.expires = expires;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public long getReExpires() {
        return reExpires;
    }

    public void setReExpires(long reExpires) {
        this.reExpires = reExpires;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Date getTokenCreationTime() {
        return tokenCreationTime;
    }

    public void setTokenCreationTime(Date tokenCreationTime) {
        this.tokenCreationTime = tokenCreationTime;
    }

}
