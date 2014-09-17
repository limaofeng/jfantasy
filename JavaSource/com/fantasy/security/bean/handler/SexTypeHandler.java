package com.fantasy.security.bean.handler;

import org.apache.commons.beanutils.Converter;

import com.fantasy.framework.dao.mybatis.type.CharEnumTypeHandler;
import com.fantasy.security.bean.enums.Sex;

public class SexTypeHandler extends CharEnumTypeHandler<Sex> {
	public SexTypeHandler() {
		super(new Converter() {

			@SuppressWarnings("rawtypes")
			public Object convert(Class classes, Object value) {
				if ((value instanceof Sex)) {
					switch (((Sex) value).ordinal()) {
					case 0:
						return Character.valueOf('U');
					case 1:
						return Character.valueOf('M');
					case 2:
						return Character.valueOf('F');
					}
					return Character.valueOf('U');
				}
				if ((value instanceof String)) {
					switch (((String) value).charAt(0)) {
					case 'F':
						return Sex.female;
					case 'M':
						return Sex.male;
						/*
						 * case 'U': return Sex.unknown;
						 */
					}
					/* return Sex.unknown; */
				}
				return null;
			}
		});
	}
}