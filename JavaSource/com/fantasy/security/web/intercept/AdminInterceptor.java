package com.fantasy.security.web.intercept;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

public class AdminInterceptor extends MethodFilterInterceptor {

	private static final long serialVersionUID = 2157465482396161916L;

	@Override
	protected String doIntercept(ActionInvocation invocation) throws Exception {
		return null;
	}

}
