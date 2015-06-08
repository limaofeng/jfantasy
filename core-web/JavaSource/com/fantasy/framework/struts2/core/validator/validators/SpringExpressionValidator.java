package com.fantasy.framework.struts2.core.validator.validators;

import com.fantasy.framework.spring.SpELUtil;
import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;


/**
 * spring expression 验证
 */
public class SpringExpressionValidator extends FieldValidatorSupport {
    
    private String expression;

    @Override
    public void validate(Object object) throws ValidationException {
        String fieldName = getFieldName();
        Object obj = null;

        try {
            obj = getFieldValue(fieldName, object);
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            // let this pass, but it will be logged right below
        }

        if (!SpELUtil.getExpression(expression).getValue(SpELUtil.createEvaluationContext(obj),Boolean.class)) {
            if (log.isDebugEnabled()){
                log.debug("Validation failed on expression " + expression + " with validated object " + object);
            }
            addFieldError(fieldName,object);
        }
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

}
