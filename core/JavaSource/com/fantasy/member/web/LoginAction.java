package com.fantasy.member.web;

import com.fantasy.framework.struts2.ActionSupport;

/**
 * 登陆Action
 */
public class LoginAction  extends ActionSupport {

	private static final long serialVersionUID = 7036400645757705811L;

	/**
     *  跳转登陆页面<br/>
     *  将spring security 的session异常转为request异常
     */
    @Override
    public String execute() throws Exception {
        Exception exception = (Exception)this.session.get("SPRING_SECURITY_LAST_EXCEPTION");
        System.out.println(exception);
        return SUCCESS;
    }
}
