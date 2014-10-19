package com.fantasy.member.web;

import com.fantasy.framework.struts2.ActionSupport;

/**
 * 密码找回
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-12-17 上午10:21:35
 * @version 1.0
 */
public class ForgotPasswdAction extends ActionSupport {

	private static final long serialVersionUID = -1216558933483171240L;

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
	 * 邮件验证
	 * 
	 * @功能描述
	 * @return
	 */
	public String valid() {
		return SUCCESS;
	}

	/**
	 * 重置密码
	 * 
	 * @功能描述
	 * @return
	 */
	public String reset() {
		return SUCCESS;
	}

}
