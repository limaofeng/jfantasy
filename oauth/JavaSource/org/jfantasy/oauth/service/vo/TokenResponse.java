package org.jfantasy.oauth.service.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.jfantasy.oauth.bean.enums.TokenType;

import java.util.Date;

public class TokenResponse {

    public TokenResponse(AccessToken accessToken) {
        this.setKey(accessToken.getKey());
        this.setType(accessToken.getType());
        this.setExpires(accessToken.getExpires());
        this.setRefreshToken(accessToken.getRefreshToken());
        this.setReExpires(accessToken.getReExpires());
        this.setScope(accessToken.getScope());
        this.setTokenCreationTime(accessToken.getTokenCreationTime());
    }

    @ApiModelProperty(name = "access_token", value = "访问令牌。通过该令牌调用需要授权类接口", required = true)
    @JsonProperty("access_token")
    private String key;
    @ApiModelProperty(name = "token_type", value = "表示令牌类型，该值大小写不敏感，必选项 ", required = true)
    @JsonProperty("token_type")
    private TokenType type;
    @ApiModelProperty(name = "expires_in", value = "访问令牌的有效时间，单位是秒。")
    @JsonProperty(value = "expires_in", required = true)
    private long expires;
    @ApiModelProperty(value = "权限范围", notes = "如果与客户端申请的范围一致，此项省略")
    private String scope;
    @ApiModelProperty(name = "refresh_token", value = "更新令牌，用来获取下一次的访问令牌")
    private String refreshToken;
    @ApiModelProperty(name = "re_expires_in", value = "刷新令牌的有效时间，单位是秒。")
    private long reExpires;
    @ApiModelProperty(name = "token_creation_time", value = "token 的创建时间。")
    private Date tokenCreationTime;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getExpires() {
        return expires;
    }

    public void setExpires(long expires) {
        this.expires = expires;
    }

    public long getReExpires() {
        return reExpires;
    }

    public void setReExpires(long reExpires) {
        this.reExpires = reExpires;
    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public Date getTokenCreationTime() {
        return tokenCreationTime;
    }

    public void setTokenCreationTime(Date tokenCreationTime) {
        this.tokenCreationTime = tokenCreationTime;
    }

}
