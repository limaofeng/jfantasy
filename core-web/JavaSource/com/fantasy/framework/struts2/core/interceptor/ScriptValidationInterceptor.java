package com.fantasy.framework.struts2.core.interceptor;

import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.web.WebUtil;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;
import com.opensymphony.xwork2.validator.ActionValidatorManager;
import com.opensymphony.xwork2.validator.Validator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import java.util.List;

public class ScriptValidationInterceptor extends MethodFilterInterceptor {
	private static final long serialVersionUID = -7982343807547662775L;
	private transient ActionValidatorManager actionValidatorManager;
	private static final Log logger = LogFactory.getLog(ScriptValidationInterceptor.class);

	@Inject
	public void setActionValidatorManager(ActionValidatorManager mgr) {
		this.actionValidatorManager = mgr;
	}

	@SuppressWarnings("rawtypes")
	protected String doIntercept(ActionInvocation invocation) throws Exception {
		if ("validata".equals(WebUtil.getExtension(ServletActionContext.getRequest()))) {
			logger.debug("生成[" + invocation.getAction().getClass().getName() + "-" + invocation.getProxy().getActionName() + "-validation.xml]对应的JavaScript验证");
			List<Validator> validators = this.actionValidatorManager.getValidators(invocation.getAction().getClass(), invocation.getProxy().getActionName());
			ServletActionContext.getRequest().setAttribute("validators", validators);
			ServletActionContext.getRequest().setAttribute("formId", StringUtil.defaultValue(ServletActionContext.getRequest().getParameter("formId"), invocation.getInvocationContext().getName()));
			ServletActionContext.getRequest().setAttribute("iErrorClass", StringUtil.defaultValue(ServletActionContext.getRequest().getParameter("iErrorClass"), null));
			ServletActionContext.getRequest().setAttribute("errorClass", StringUtil.defaultValue(ServletActionContext.getRequest().getParameter("errorClass"), null));
			ServletActionContext.getRequest().setAttribute("correctClass", StringUtil.defaultValue(ServletActionContext.getRequest().getParameter("correctClass"), null));
			return "script";
		}
		return invocation.invoke();
	}
}