package org.jfantasy.cms.bean.converter;

import org.jfantasy.cms.bean.Content;
import org.jfantasy.common.bean.converter.HtmlConverter;
import org.jfantasy.framework.util.common.ClassUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.system.util.SettingUtil;
import ognl.DefaultTypeConverter;

import java.lang.reflect.Array;
import java.lang.reflect.Member;
import java.util.Map;

public class ContentConverter extends DefaultTypeConverter {

    private HtmlConverter htmlConverter = new HtmlConverter();

    @SuppressWarnings("rawtypes")
    public Object convertValue(Map context, Object target, Member member, String propertyName, Object value, Class toType) {
        if (Content.class.isAssignableFrom(toType)) {
            Object _value = htmlConverter.convertValue(context, target, member, propertyName, StringUtil.nullValue(ClassUtil.isArray(value) ? Array.get(value, 0) : value), String.class);
            return new Content(_value.toString());
        } else if (String.class.isAssignableFrom(toType)) {
            return SettingUtil.toHtml(value.toString());
        }
        return super.convertValue(context, target, member, propertyName, value, toType);
    }

}
