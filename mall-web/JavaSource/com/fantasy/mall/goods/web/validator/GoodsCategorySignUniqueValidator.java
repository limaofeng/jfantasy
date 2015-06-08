package com.fantasy.mall.goods.web.validator;

import org.springframework.beans.factory.annotation.Autowired;

import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.mall.goods.service.GoodsService;
import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

/**
 * @Author lsz
 * @Date 2013-12-21 下午2:52:20
 * 
 */
public class GoodsCategorySignUniqueValidator extends FieldValidatorSupport {

	@Autowired
	private GoodsService goodsService;

	@Override
	public void validate(Object obj) throws ValidationException {
		String fieldName = getFieldName();
		String id = StringUtil.nullValue(getFieldValue("id", obj));
		String sign = StringUtil.nullValue(getFieldValue(fieldName, obj));
		if (!goodsService.goodsCategorySignUnique(sign, StringUtil.isBlank(id) ? null : Long.valueOf(id))) {
			addFieldError(fieldName, obj);
		}
	}

}
