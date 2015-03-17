package com.fantasy.common.bean.converter;

import java.lang.reflect.Array;
import java.lang.reflect.Member;
import java.util.Map;

import javax.annotation.Resource;

import ognl.DefaultTypeConverter;

import com.fantasy.common.service.AreaService;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.jackson.JSON;

public class AreaStoreConverter extends DefaultTypeConverter {

	@Autowired
	public AreaService areaService;

	@SuppressWarnings("rawtypes")
	public Object convertValue(Map context, Object target, Member member, String propertyName, Object value, Class toType) {
		if (String.class.isAssignableFrom(toType)) {
			String id = StringUtil.nullValue(ClassUtil.isArray(value) ? Array.get(value, 0) : value);
			return StringUtil.isNotBlank(id) ? JSON.serialize(areaService.get(id)) : "";
		}
		return super.convertValue(context, target, member, propertyName, value, toType);
	}

}