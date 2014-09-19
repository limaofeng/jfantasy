package com.fantasy.framework.util.common;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class BeanUtil {

	private static final Log logger = LogFactory.getLog(BeanUtil.class);

	public static void setValue(Object target, String fieldName, Object value) {
		ClassUtil.setValue(target, fieldName, value);
	}

	public static Object getValue(Object target, String fieldName) {
		return ClassUtil.getValue(target, fieldName);
	}

    public static <T> T copyProperties(T dest, T orig) {
        try {
            BeanUtils.copyProperties(dest,orig);
            return dest;
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage(),e);
            return null;
        } catch (InvocationTargetException e) {
            logger.error(e.getMessage(),e);
            return null;
        }
    }

    /*
    @Deprecated
	public static <T> T copy(T dest, Object orig, String... excludeProperties) {
		return copy(dest, orig, "", excludeProperties);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
    @Deprecated
	private static <T> T copy(T dest, Object orig, String superName, String[] excludeProperties) {
		try {
			for (Map.Entry<String, Object> entry : OgnlUtil.getInstance().getBeanMap(orig, excludeProperties).entrySet()) {
				if (RegexpUtil.find(superName.concat((String) entry.getKey()), excludeProperties))
					continue;
				if (ObjectUtil.isNull(entry.getValue()))
					continue;
				Property property = ClassUtil.getProperty(dest, (String) entry.getKey());
				if (ObjectUtil.isNull(property))
					continue;
				if (!property.isWrite())
					continue;
//				if (logger.isDebugEnabled())
//					logger.debug(superName + "=>" + entry.getKey());
				if (entry.getValue().getClass().isEnum() || ClassUtil.isPrimitiveOrWrapperOrStringOrDate(entry.getValue().getClass())) {
					OgnlUtil.getInstance().setValue((String) entry.getKey(), dest, entry.getValue());
				} else if (ClassUtil.isList(property.getPropertyType())) {
					List<Object> list = new ArrayList<Object>();
					OgnlUtil.getInstance().setValue((String) entry.getKey(), dest, list);
					int length = length(entry.getValue());
					for (int i = 0; i < length; i++) {
						String _superName = superName + entry.getKey() + "[" + String.valueOf(i) + "]" + ".";
						list.add(copy(ClassUtil.newInstance((Class) ClassUtil.getMethodGenericParameterTypes(property.getWriteMethod().getMethod()).get(0)), get(entry.getValue(), i), _superName, excludeProperties));
					}
				} else if (ClassUtil.isArray(property.getPropertyType())) {
					Object object = OgnlUtil.getInstance().getValue((String) entry.getKey(), dest);
					Object array = Array.newInstance(property.getPropertyType(), Array.getLength(object));
					for (int i = 0; i < Array.getLength(object); i++)
						Array.set(array, i, copy(ClassUtil.newInstance((Class) ClassUtil.getMethodGenericParameterTypes(property.getWriteMethod().getMethod()).get(0)), get(entry.getValue(), i), superName.concat((String) entry.getKey()).concat("[").concat(String.valueOf(i)).concat("]").concat("."), excludeProperties));
				} else {
					String _superName = superName + entry.getKey() + ".";
					Object object = OgnlUtil.getInstance().getValue((String) entry.getKey(), dest);
					if (object == null) {
						OgnlUtil.getInstance().setValue((String) entry.getKey(), dest, copy(ClassUtil.newInstance(property.getPropertyType()), entry.getValue(), _superName, excludeProperties));
					} else
						copy(object, entry.getValue(), _superName, excludeProperties);
				}
			}
		} catch (IntrospectionException e) {
			logger.debug(e.getMessage(), e);
		} catch (OgnlException e) {
			logger.debug(e.getMessage(), e);
		}
		return dest;
	}
	*/

	private static int length(Object value) {
		if (ClassUtil.isArray(value))
			return Array.getLength(value);
		if (ClassUtil.isList(value)) {
			return ((List<?>) value).size();
		}
		return 0;
	}

	private static Object get(Object value, int i) {
		if (ClassUtil.isArray(value))
			return Array.get(value, i);
		if (ClassUtil.isList(value)) {
			return ((List<?>) value).get(i);
		}
		return null;
	}
}