package org.jfantasy.wx.framework.oauth2;

/**
 * 访问标识
 */
public class AccessToken {
    /**
     * 网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
     */
    private String token;
    /**
     * access_token接口调用凭证超时时间，单位（秒）
     */
    private int expiresIn = -1;
    /**
     * 用户刷新access_token
     */
    private String refreshToken;
    /**
     * 用户唯一标识，请注意，在未关注公众号时，用户访问公众号的网页，也会产生一个用户和公众号唯一的OpenID
     */
    private String openId;
    /**
     * 用户授权的作用域，使用逗号（,）分隔
     */
    private Scope scope;

    public AccessToken(String accessToken, int expiresIn, String refreshToken, String openId, String scope) {
        this.token = accessToken;
        this.expiresIn = expiresIn;
        this.refreshToken = refreshToken;
        this.openId = openId;
        this.scope = Scope.valueOf(scope.replaceAll("snsapi_",""));
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }
}
