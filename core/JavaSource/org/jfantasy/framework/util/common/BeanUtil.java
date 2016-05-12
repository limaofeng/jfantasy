package org.jfantasy.framework.util.common;

import org.jfantasy.framework.util.ognl.OgnlUtil;
import org.jfantasy.framework.util.reflect.Property;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BeanUtil {
    private BeanUtil() {
    }

    public static void setValue(Object target, String fieldName, Object value) {
        ClassUtil.setValue(target, fieldName, value);
    }

    public static Object getValue(Object target, String fieldName) {
        return ClassUtil.getValue(target, fieldName);
    }

    public static <T> T copyProperties(T dest, Object orig, String... excludeProperties) {
        if (dest == null || orig == null) {
            return dest;
        }
        Class destClass = dest.getClass();
        Property[] properties = ClassUtil.getPropertys(orig);
        for (Property property : properties) {
            if (ObjectUtil.indexOf(excludeProperties, property.getName()) != -1) {
                continue;
            }
            if (!property.isRead()) {
                continue;
            }
            Property setProperty = ClassUtil.getProperty(destClass, property.getName());
            if (setProperty == null || !setProperty.isWrite()) {
                continue;
            }
            if (ClassUtil.isBasicType(property.getPropertyType()) && ClassUtil.isBasicType(setProperty.getPropertyType())) {
                Object o = property.getValue(orig);
                if (o == null) {
                    continue;
                }
                if (Boolean.class.isAssignableFrom(property.getPropertyType())
                        || boolean.class.isAssignableFrom(property.getPropertyType())
                        || Date.class.isAssignableFrom(property.getPropertyType())) {
                    OgnlUtil.getInstance().setValue(setProperty.getName(), dest, o);
                } else {
                    OgnlUtil.getInstance().setValue(setProperty.getName(), dest, o);
                }
                continue;
            }
            if (!property.getPropertyType().equals(setProperty.getPropertyType())) {
                continue;
            }
            Object o = property.getValue(orig);
            if (o == null) {
                continue;
            }
            setProperty.setValue(dest, o);
        }
        return dest;
    }

    public static <T> T copyNotNull(T dest, Object orig) {
        List<String> excludeProperties = new ArrayList<String>();
        Property[] properties = ClassUtil.getPropertys(orig);
        for (Property property : properties) {
            if (!property.isRead() || property.getValue(orig) == null) {
                excludeProperties.add(property.getName());
            }
        }
        return copyProperties(dest, orig, excludeProperties.toArray(new String[excludeProperties.size()]));
    }

    private static int length(Object value) {
        if (ClassUtil.isArray(value)) {
            return Array.getLength(value);
        }
        if (ClassUtil.isList(value)) {
            return ((List<?>) value).size();
        }
        return 0;
    }

    private static Object get(Object value, int i) {
        if (ClassUtil.isArray(value)) {
            return Array.get(value, i);
        }
        if (ClassUtil.isList(value)) {
            return ((List<?>) value).get(i);
        }
        return null;
    }
}