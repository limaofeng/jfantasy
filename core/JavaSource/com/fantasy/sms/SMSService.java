package com.fantasy.sms;

/**
 * 短信发送接口
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-7-4 上午10:16:02
 */
public interface SMSService {

    boolean send(String phone, String value);

}
