package com.fantasy.attr.framework.converter;

import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.common.DateUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.ognl.typeConverter.DateFormat;
import ognl.DefaultTypeConverter;

import java.lang.reflect.Array;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

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
        } else if (ClassUtil.isArray(value)) {
            StringBuilder buffer = new StringBuilder();
            for (int i = 0, len = Array.getLength(value); i < len; i++) {
                buffer.append(super.convertValue(context, target, member, propertyName, Array.get(value, i), toType));
                if (i != len - 1) {
                    buffer.append(";");//TODO 如果输入的文字中存在相同的分割符。将造成数据问题
                }
            }
            return buffer.toString();
        } else if (ClassUtil.isArray(toType)) {
            String[] array = StringUtil.tokenizeToStringArray(value.toString(), ";");
            Object ret = Array.newInstance(toType.getComponentType(), array.length);
            for (int i = 0; i < array.length; i++) {
                Array.set(ret, i, super.convertValue(context, target, member, propertyName, array[i], toType.getComponentType()));
            }
            return ret;
        }
        return super.convertValue(context, target, member, propertyName, value, toType);
    }
}
