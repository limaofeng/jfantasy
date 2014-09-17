package com.fantasy.framework.util.cglib.interceptor;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import com.fantasy.framework.util.common.JavassistUtil;
import com.fantasy.framework.util.validator.DefaultValidatorContext;
import com.fantasy.framework.util.validator.Validateable;
import com.fantasy.framework.util.validator.ValidatorContext;
import com.fantasy.framework.util.validator.exception.StackValidationException;
import com.fantasy.framework.util.validator.exception.Error;

public class ValidatorInterceptor implements MethodInterceptor {
	public Object intercept(Object target, Method method, Object[] params, MethodProxy proxy) throws Throwable {
		ValidatorContext context = DefaultValidatorContext.getInstance();
		String methodname = method.getName();
		String classname = target.getClass().getName();

		if (classname.indexOf("$$") > -1) {
			classname = classname.substring(0, classname.indexOf("$$"));
		}
		Class<?> classes = Class.forName(classname);

		Validateable validateable = context.getValidateable(classes);

		String[] paramNames = JavassistUtil.getParamNames(classname, methodname, method.getParameterTypes());
		ParameterMap paramMap = new ParameterMap();
		int i = 0;
		for (int len = paramNames.length; i < len; i++) {
			paramMap.put(paramNames[i], params[i]);
		}
		Map<String, List<String>> errorMap = validateable.validate(methodname, paramMap);
		if (!errorMap.isEmpty()) {
			StackValidationException exception = new StackValidationException("参数验证失败!");
			for (Map.Entry<String, List<String>> entry : errorMap.entrySet()) {
				exception.addError(new Error((String) entry.getKey(), ((List<String>) entry.getValue()).get(0)));
			}
			throw exception;
		}
		return proxy.invokeSuper(target, params);
	}
}