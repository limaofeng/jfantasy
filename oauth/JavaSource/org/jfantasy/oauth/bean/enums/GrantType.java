package org.jfantasy.oauth.bean.enums;

public enum GrantType {
    /**
     * 客户端
     */
    client_credentials,
    /**
     * 获取新的 code
     */
    authorization_code,
    /**
     * 刷新 token 简化
     */
    implicit,
    /**
     * 密码
     */
    password,
    /**
     * 更新令牌
     */
    refresh_token
}
