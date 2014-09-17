package com.fantasy.member.web;

import com.fantasy.framework.struts2.ActionSupport;

/**
 * 邮箱认证
 * 
 * @功能描述 用于验证邮箱是否邮箱及用户与邮箱的绑定操作
 * @author 李茂峰
 * @since 2013-12-17 上午10:08:49
 * @version 1.0
 */
public class ValidateMailAction extends ActionSupport {

	private static final long serialVersionUID = 1373920921276560529L;

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
