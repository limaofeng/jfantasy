package org.jfantasy.member.service;

/**
 * 短信验证码接口
 */
public interface SMSCodeEncoder {

    boolean matches(String phone, String encodedPassword);

}
