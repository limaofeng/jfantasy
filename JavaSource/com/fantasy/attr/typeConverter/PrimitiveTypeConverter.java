package com.fantasy.attr.typeConverter;

import java.lang.reflect.Array;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.common.DateUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.ognl.typeConverter.DateFormat;

import ognl.DefaultTypeConverter;

public class PrimitiveTypeConverter extends DefaultTypeConverter {
	
	@SuppressWarnings("rawtypes")
	public Object convertValue(Map context, Object target, Member member, String propertyName, Object value, Class toType) {
		if (Date.class.isAssignableFrom(toType)) {
			String dateFormatString = null;
			try {
				DateFormat dateFormat = (DateFormat) ClassUtil.getParamAnno((Method) member);
				dateFormatString = dateFormat.pattern();
			} catch (Exception e) {
				dateFormatString = "yyyy-MM-dd";
			}
			return DateUtil.parse(StringUtil.nullValue(ClassUtil.isArray(value) ? Array.get(value, 0) : value), dateFormatString);
		}
		return super.convertValue(context, target, member, propertyName, value, toType);
	}
}
