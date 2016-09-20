package org.jfantasy.oauth.service.vo;

import org.jfantasy.oauth.bean.enums.GrantType;
import org.jfantasy.oauth.bean.enums.TokenType;

import java.io.Serializable;
import java.util.Date;

public class AccessToken implements Serializable{

    private static final long serialVersionUID = -3121930431297681821L;

    private String key;
    /**
     * 授权模式
     */
    private GrantType grantType;
    /**
     * 类型
     */
    private TokenType type;
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

    public GrantType getGrantType() {
        return grantType;
    }

    public void setGrantType(GrantType grantType) {
        this.grantType = grantType;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
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
