package com.fantasy.system.bean.typeConverter;

import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.system.bean.DataDictionaryKey;
import ognl.DefaultTypeConverter;

import java.lang.reflect.Array;
import java.lang.reflect.Member;
import java.util.Map;

public class DataDictionaryKeyConverter extends DefaultTypeConverter {

	@SuppressWarnings("rawtypes")
	public Object convertValue(Map context, Object target, Member member, String propertyName, Object value, Class toType) {
		if (DataDictionaryKey.class.isAssignableFrom(toType)) {
			String key = StringUtil.nullValue(ClassUtil.isArray(value) ? Array.get(value, 0) : value);
			return DataDictionaryKey.newInstance(key.split(":")[1], key.split(":")[0]);
		}
		return super.convertValue(context, target, member, propertyName, value, toType);
	}

}