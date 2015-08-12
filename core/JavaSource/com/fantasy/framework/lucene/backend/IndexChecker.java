package com.fantasy.framework.lucene.backend;

import java.lang.reflect.Field;

import org.apache.log4j.Logger;

import com.fantasy.framework.lucene.annotations.BoostSwitch;
import com.fantasy.framework.lucene.annotations.IndexEmbed;
import com.fantasy.framework.lucene.annotations.IndexEmbedList;
import com.fantasy.framework.lucene.annotations.IndexFilter;
import com.fantasy.framework.lucene.annotations.IndexProperty;
import com.fantasy.framework.lucene.annotations.IndexRef;
import com.fantasy.framework.lucene.annotations.IndexRefBy;
import com.fantasy.framework.lucene.annotations.IndexRefList;
import com.fantasy.framework.lucene.annotations.Indexed;
import com.fantasy.framework.lucene.cache.FieldsCache;
import com.fantasy.framework.lucene.exception.FieldException;

public class IndexChecker {
    private IndexChecker() {
    }

    private static final Logger LOGGER = Logger.getLogger(IndexChecker.class);

    /**
     * 判断Class是否标注了 @Indexed 注解
     *
     * @param clazz
     * @return
     * @功能描述
     */
    public static boolean hasIndexed(Class<?> clazz) {
        return clazz.getAnnotation(Indexed.class) != null;
    }

    public static boolean hasIndexAnnotation(Class<?> clazz, String key) {
        boolean result = false;
        int index = key.indexOf(".");
        if (index != -1) {
            key = key.substring(0, index);
        }
        Field field = null;
        try {
            field = FieldsCache.getInstance().getField(clazz, key);
        } catch (FieldException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        if ((field.getAnnotation(IndexProperty.class) != null) || (field.getAnnotation(IndexEmbed.class) != null) || (field.getAnnotation(IndexEmbedList.class) != null) || (field.getAnnotation(IndexRef.class) != null) || (field.getAnnotation(IndexRefList.class) != null) || (field.getAnnotation(IndexRefBy.class) != null) || (field.getAnnotation(BoostSwitch.class) != null) || (field.getAnnotation(IndexFilter.class) != null)) {
            result = true;
        }
        return result;
    }

    /**
     * 判断Class是否标注@indexed注解或者属性是否标注了@IndexRefBy注解
     *
     * @param clazz
     * @return
     * @功能描述
     */
    public static boolean needListener(Class<?> clazz) {
        boolean result = false;
        if (clazz.getAnnotation(Indexed.class) != null) {
            result = true;
        } else {
            Field[] fields = FieldsCache.getInstance().get(clazz);
            for (Field f : fields) {
                IndexRefBy irb = (IndexRefBy) f.getAnnotation(IndexRefBy.class);
                if (irb != null) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }
}
