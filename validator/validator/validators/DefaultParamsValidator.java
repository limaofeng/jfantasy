package org.jfantasy.framework.util.validator.validators;

import org.jfantasy.framework.util.validator.ParamsValidator;
import org.jfantasy.framework.util.validator.exception.ValidationException;

import java.util.Map;

public abstract class DefaultParamsValidator implements ParamsValidator {
    private Map<String, String> params = null;

    public void validate(Object object) throws ValidationException {
        validate(object, this.params);
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public Map<String, String> getParams() {
        return this.params;
    }
}