package com.fantasy.framework.struts2.validator.validators;

import java.lang.reflect.Array;
import java.util.Map;

import com.fantasy.framework.struts2.context.ActionConstants;
import com.fantasy.framework.struts2.interceptor.ParametersInterceptor.MethodParam;
import com.fantasy.framework.util.common.ClassUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

@Deprecated
public abstract class FantasyFieldValidatorSupport extends FieldValidatorSupport{

	@Override
	@SuppressWarnings("unchecked")
	public Object getFieldValue(String fieldName, Object object) throws ValidationException {
		MethodParam root = (MethodParam) ActionContext.getContext().get(ActionConstants.METHOD_PARAM);
		Map<String, Object> params = root.getParams();
		
		if(params.size() == 0){
			ValueStack stack = (ValueStack) ClassUtil.getValue(this, "stack");
			Map<String, Object> parameters = (Map<String, Object>) stack.findValue("#parameters");
			if (parameters.containsKey(fieldName)) {
				Object array = parameters.get(fieldName);
				if ((ClassUtil.isArray(array)) && (Array.getLength(array) > 0)) {
					return Array.get(array, 0).toString();
				}
			}
		}
		Object retVal = super.getFieldValue(fieldName, params.size() == 1 ? root.getArgs()[0] : params);
		if (retVal == null) {
			ValueStack stack = (ValueStack) ClassUtil.getValue(this, "stack");
			Map<String, Object> parameters = (Map<String, Object>) stack.findValue("#parameters");
			return parameters.containsKey(fieldName) ? retVal : parameters.containsKey(fieldName) ;
		}
		return retVal;
	}
	
}
