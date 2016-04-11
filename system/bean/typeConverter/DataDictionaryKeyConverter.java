package org.jfantasy.system.bean.typeConverter;

import ognl.DefaultTypeConverter;
import org.jfantasy.framework.util.common.ClassUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.system.bean.DataDictionaryKey;

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