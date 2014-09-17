package com.fantasy.member.web;

import com.fantasy.framework.struts2.ActionSupport;

/**
 * 手机认证
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-12-17 上午10:08:33
 * @version 1.0
 */
public class ValidateMobileAction extends ActionSupport {

	private static final long serialVersionUID = 4108474128041266285L;

	/**
	 * 发送验证邮件
	 * 
	 * @功能描述
	 * @return
	 */
	public String send() {
		return SUCCESS;
	}

	/**
	 * 绑定邮箱
	 * 
	 * @功能描述
	 * @return
	 */
	public String bind() {
		return SUCCESS;
	}

}
