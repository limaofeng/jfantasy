package com.fantasy.attr.framework.converter;

import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.common.DateUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.ognl.typeConverter.DateFormat;
import ognl.DefaultTypeConverter;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Array;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

public class PrimitiveTypeConverter extends DefaultTypeConverter {

    private final static Log LOG = LogFactory.getLog(PrimitiveTypeConverter.class);

    @SuppressWarnings("rawtypes")
    public Object convertValue(Map context, Object target, Member member, String propertyName, Object value, Class toType) {
        if (value != null && Date.class.isAssignableFrom(value.getClass()) && toType == String.class) {
            return DateUtil.format((Date)value,"yyyy-MM-dd HH:mm:ss");
        }else if (Date.class.isAssignableFrom(toType)) {
            try {
                DateFormat dateFormat = (DateFormat) ClassUtil.getParamAnno((Method) member);
                return DateUtil.parse(StringUtil.nullValue(ClassUtil.isArray(value) ? Array.get(value, 0) : value), dateFormat.pattern());
            } catch (RuntimeException e) {
                LOG.error(e.getMessage(),e);
                return ConvertUtils.convert(value,Date.class);
            }
        } else if (ClassUtil.isArray(value)) {
            StringBuilder buffer = new StringBuilder();
            for (int i = 0, len = Array.getLength(value); i < len; i++) {
                buffer.append(this.convertValue(context, target, member, propertyName, Array.get(value, i), toType));
                if (i != len - 1) {
                    buffer.append(";");
                }
            }
            return buffer.toString();
        } else if (ClassUtil.isArray(toType)) {
            assert value != null;
            String[] array = StringUtil.tokenizeToStringArray(value.toString(), ";");
            Object ret = Array.newInstance(toType.getComponentType(), array.length);
            for (int i = 0; i < array.length; i++) {
                Array.set(ret, i, this.convertValue(context, target, member, propertyName, array[i], toType.getComponentType()));
            }
            return ret;
        }
        return super.convertValue(context, target, member, propertyName, value, toType);
    }
}