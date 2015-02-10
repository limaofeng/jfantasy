package com.fantasy.framework.util.validator.validators;

import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.regexp.RegexpUtil;
import com.fantasy.framework.util.validator.exception.ValidationException;
import java.util.Map;

public class NumberFieldValidator extends DefaultParamsValidator {
	public void validate(Object object, Map<String, String> params) throws ValidationException {
		String regexp = (String) params.get("regexp");

		if (StringUtil.isBlank(object)){
            return;
        }
		if (!RegexpUtil.isMatch(StringUtil.nullValue(object), StringUtil.nullValue(regexp))) {
			throw new ValidationException("{value:" + object + ",regexp:" + regexp + "}验证失败!");
		}
		String[] numbers = object.toString().split("\\.");
		String decimal = numbers.length > 1 ? numbers[1] : "";
		String integerBit = numbers[0];
		Long scale = Long.valueOf(StringUtil.isNotBlank((String) params.get("scale")) ? Long.valueOf((String) params.get("scale")).longValue() : 0L);
		if (StringUtil.isNotBlank((String) params.get("length"))) {
			Long length = Long.valueOf((String) params.get("length"));
			if (integerBit.length() > length.longValue() - scale.longValue()) {
				throw new ValidationException("数值超过最大位数[NUMBER(" + length + "," + scale + ")]");
			}
			if (decimal.length() > scale.longValue()) {
				throw new ValidationException("数值超过最大精度[NUMBER(" + length + "," + scale + ")]");
			}
		}
		if (decimal.length() > scale.longValue()){
            throw new ValidationException("数值超过最大精度[NUMBER(," + scale + ")]");
        }
	}
}