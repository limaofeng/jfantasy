package com.fantasy.framework.lucene.mapper;

import java.lang.reflect.Field;

import org.apache.log4j.Logger;

import com.fantasy.framework.lucene.cache.FieldsCache;
import com.fantasy.framework.util.ognl.OgnlUtil;

public class FieldUtil {
	
	private static final Logger logger = Logger.getLogger(FieldUtil.class);

	public static Object get(Object obj, Field f) {
		return get(obj, f.getName());
	}

	public static Object get(Object obj, String fName) {
		return OgnlUtil.getInstance().getValue(fName, obj);
	}

	public static void set(Object obj, Field f, Object value) {
		set(obj, f.getName(), value);
	}

	public static void set(Object obj, String fName, Object value) {
		OgnlUtil.getInstance().setValue(fName, obj, value);
	}

	public static void copy(Object src, Object target) {
		if ((src == null) || (target == null)) {
			return;
		}
		Field[] fields = FieldsCache.getInstance().get(src.getClass());
		for (Field f : fields) {
			Object value = get(src, f);
			set(target, f, value);
		}
	}

	public static Class<?> getRealType(Field field) {
		Class<?> clazz = field.getType();
		return getRealType(clazz);
	}

	public static Class<?> getRealType(Class<?> clazz) {
		Class<?> cls = clazz;
		if (clazz.isInterface()) {
			logger.error("The implementation of interface " + clazz.toString() + " is not specified.");
		}
		return cls;
	}

}
