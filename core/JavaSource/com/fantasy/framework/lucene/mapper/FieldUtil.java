package com.fantasy.framework.lucene.mapper;

import com.fantasy.framework.lucene.cache.FieldsCache;
import com.fantasy.framework.util.ognl.OgnlUtil;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;

public class FieldUtil {

    private FieldUtil(){}
    private static final Logger LOGGER = Logger.getLogger(FieldUtil.class);

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

    public static Class getRealType(Field field) {
        return getRealType(field.getType());
    }

    public static Class getRealType(Class clazz) {
        if (clazz.isInterface()) {
            LOGGER.error("The implementation of interface " + clazz.toString() + " is not specified.");
        }
        return clazz;
    }

}
