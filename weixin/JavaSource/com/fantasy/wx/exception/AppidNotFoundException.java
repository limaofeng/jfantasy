package com.fantasy.wx.exception;

/**
 * 微信账号不存在
 */
public class AppidNotFoundException extends WeiXinException {

    public AppidNotFoundException(String message) {
        super(message);
    }
}
