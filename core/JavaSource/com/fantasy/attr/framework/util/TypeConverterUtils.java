package com.fantasy.attr.framework.util;

import com.fantasy.attr.storage.bean.Converter;
import com.fantasy.attr.storage.service.ConverterService;
import com.fantasy.framework.spring.SpringContextUtil;
import ognl.TypeConverter;
import org.hibernate.criterion.Restrictions;

public class TypeConverterUtils {
    private TypeConverterUtils() {
    }

    private static ConverterService converterService;

    private static ConverterService getConverterService() {
        return converterService != null ? converterService : (converterService = SpringContextUtil.getBeanByType(ConverterService.class));
    }

    public static Converter getTypeConverter(Class<? extends TypeConverter> typeConverter) {
        Converter converter = getConverterService().findUnique(Restrictions.eq("typeConverter", typeConverter.getName()));
        if (converter == null) {
            throw new RuntimeException(" TypeConverter class " + typeConverter + " 未配置 ");
        }
        return converter;
    }

}
