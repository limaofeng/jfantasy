package com.fantasy.security.userdetails.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 自定义异常: 首次登陆异常
 *
 * @author 李茂峰
 */
public class FirstLoginAuthenticationException extends AuthenticationException {

    public FirstLoginAuthenticationException(String msg) {
        super(msg);
    }

    private static final long serialVersionUID = 8697821657285909284L;

}
