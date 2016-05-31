package org.jfantasy.oauth.bean.enums;

import io.swagger.annotations.ApiModelProperty;

public enum GrantType {
    @ApiModelProperty("客户端")
    client_credentials,
    /**
     * 获取新的 code
     */
    @ApiModelProperty("授权码")
    authorization_code,
    /**
     * 刷新 token
     */
    @ApiModelProperty("简化")
    implicit,
    @ApiModelProperty("密码")
    password,
    @ApiModelProperty("更新令牌")
    refresh_token
}
