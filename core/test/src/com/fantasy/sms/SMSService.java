package com.fantasy.sms;

/**
 * 短信发送接口
 * @功能描述 
 * @author 李茂峰
 * @since 2013-7-4 上午10:16:02
 * @version 1.0
 */
public interface SMSService {

	public boolean send(String phone,String value);

}
