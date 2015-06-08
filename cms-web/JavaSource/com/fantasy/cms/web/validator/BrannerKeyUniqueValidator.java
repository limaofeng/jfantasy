package com.fantasy.cms.web.validator;
import org.springframework.beans.factory.annotation.Autowired;
import com.fantasy.cms.service.BannerService;
import com.fantasy.framework.util.common.StringUtil;
import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;



public class BrannerKeyUniqueValidator extends FieldValidatorSupport {

	@Autowired
	private BannerService bannerService;

	@Override
	public void validate(Object obj) throws ValidationException {
		String fieldName = getFieldName();
		String id = StringUtil.nullValue(getFieldValue("id", obj));
		String key = StringUtil.nullValue(getFieldValue(fieldName, obj));
		if (!bannerService.bannerKeyUnique(key, StringUtil.isBlank(id) ? null : Long.valueOf(id))) {
			addFieldError(fieldName, obj);
		}
	}

}