package com.fantasy.framework.struts2.interceptor;

import com.fantasy.framework.struts2.context.ActionConstants;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.reflect.MethodProxy;
import com.fantasy.framework.util.web.WebUtil;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ValidationAware;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;
import com.opensymphony.xwork2.interceptor.ValidationWorkflowAware;
import com.opensymphony.xwork2.interceptor.annotations.InputConfig;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;

import java.lang.reflect.Method;

public class FantasyWorkflowInterceptor extends MethodFilterInterceptor {

	private static final long serialVersionUID = -4228178923149198375L;

	private static final Logger LOG = LoggerFactory.getLogger(FantasyWorkflowInterceptor.class);

	private String inputResultName = Action.INPUT;

	public void setInputResultName(String inputResultName) {
		this.inputResultName = inputResultName;
	}

	protected String doIntercept(ActionInvocation invocation) throws Exception {
		Object action = invocation.getAction();

		if (action instanceof ValidationAware) {
			ValidationAware validationAwareAction = (ValidationAware) action;
			if (validationAwareAction.hasErrors()) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("Errors on action " + validationAwareAction + ", returning result name 'input'");
				}
				String resultName = WebUtil.isAjax() ? ActionConstants.JSONDATA : inputResultName;
				if (action instanceof ValidationWorkflowAware) {
					resultName = ((ValidationWorkflowAware) action).getInputResultName();
				}
				InputConfig annotation = getActionMethod(action.getClass(), invocation.getProxy().getMethod()).getAnnotation(InputConfig.class);
				if (annotation != null) {
					if (!annotation.methodName().equals("")) {
						Method method = action.getClass().getMethod(annotation.methodName());
						resultName = (String) method.invoke(action);
					} else {
						resultName = annotation.resultName();
					}
				}
				return resultName;
			}
		}
		return invocation.invoke();
	}

	protected Method getActionMethod(Class<?> actionClass, String methodName) throws NoSuchMethodException {
		Method method;
		try {
			MethodProxy methodProxy = ClassUtil.getMethodProxy(actionClass, methodName);
			if (methodProxy == null) {
				throw new NoSuchMethodException();
			}
			method = methodProxy.getMethod();
		} catch (NoSuchMethodException e) {
			// hmm -- OK, try doXxx instead
			try {
				String altMethodName = "do" + methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
				method = actionClass.getMethod(altMethodName, new Class[0]);
			} catch (NoSuchMethodException e1) {
				// throw the original one
				throw e;
			}
		}
		return method;
	}
}
