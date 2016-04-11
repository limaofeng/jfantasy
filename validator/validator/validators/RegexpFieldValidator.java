package org.jfantasy.framework.util.validator.validators;

import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.regexp.RegexpUtil;
import org.jfantasy.framework.util.validator.exception.ValidationException;

import java.util.Map;

public class RegexpFieldValidator extends DefaultParamsValidator {
    public void validate(Object object, Map<String, String> params) throws ValidationException {
        String regexp = (String) params.get("regexp");
        if (!RegexpUtil.isMatch(StringUtil.nullValue(object), StringUtil.nullValue(regexp))) {
            throw new ValidationException("{value:" + object + ",regexp:" + regexp + "}验证失败!");
        }
    }
}