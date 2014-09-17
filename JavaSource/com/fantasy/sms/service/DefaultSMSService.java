package com.fantasy.sms.service;

import com.fantasy.framework.httpclient.HttpClientUtil;
import com.fantasy.framework.httpclient.Request;
import com.fantasy.framework.httpclient.Response;
import com.fantasy.framework.service.SMSService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DefaultSMSService implements SMSService {

	// 短信发送后返回值 说　明
	// -1 没有该用户账户
	// -2 密钥不正确（不是用户密码）
	// -3 短信数量不足
	// -11 该用户被禁用
	// -14 短信内容出现非法字符
	// -41 手机号码为空
	// -42 短信内容为空
	// 大于0 短信发送数量
	public boolean send(String phone, String value) {
        System.out.println(phone + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + value);
//		try {
//			Request request = new Request();
//			request.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=gbk");
//			Map<String, String> data = new HashMap<String, String>();
//			data.put("Uid", "haolue");// 注册的用户名
//			data.put("Key", "5b5a3b1aa66bed8af6b4");// 注册成功后,登录网站使用的密钥，这个密钥要登录到国建网然后有一个API接口，点进去就有一个key，可以改，那个才是密钥
//			data.put("smsMob", phone);// 手机号码
//			data.put("smsText", value);// 设置短信内容
//			request.setRequestBody(data);
//			Response response = HttpClientUtil.doPost("http://sms.webchinese.cn/web_api/", request);
//			return Integer.valueOf(response.getText("utf-8")) > 0;
            return true;
//		} catch (IOException e) {
//			return false;
//		}
	}

}
