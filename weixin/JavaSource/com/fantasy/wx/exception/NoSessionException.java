package com.fantasy.wx.exception;

/**
 * session 不存在
 */
public class NoSessionException extends WeiXinException {

    public NoSessionException(String message) {
        super(message);
    }

}
