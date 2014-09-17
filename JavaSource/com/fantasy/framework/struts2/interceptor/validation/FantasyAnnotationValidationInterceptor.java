package com.fantasy.framework.struts2.interceptor.validation;

import java.lang.reflect.Method;

import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.reflect.MethodProxy;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.validator.ActionValidatorManager;

@SuppressWarnings("rawtypes")
public class FantasyAnnotationValidationInterceptor extends org.apache.struts2.interceptor.validation.AnnotationValidationInterceptor {

	private static final long serialVersionUID = 1813272797367431184L;

	@Override
	@Inject("fantasy.actionValidatorManager")
	public void setActionValidatorManager(ActionValidatorManager mgr) {
		super.setActionValidatorManager(mgr);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Method getActionMethod(Class actionClass, String methodName) throws NoSuchMethodException {
		Method method;
		try {
			MethodProxy methodProxy = ClassUtil.getMethod(actionClass, methodName);
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