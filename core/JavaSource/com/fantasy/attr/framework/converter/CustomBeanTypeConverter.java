package com.fantasy.attr.framework.converter;

import com.fantasy.attr.framework.CustomBean;
import com.fantasy.attr.storage.service.CustomBeanService;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.common.StringUtil;
import ognl.DefaultTypeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by hebo on 2015/3/20.
 * 动态bean转换
 */
public class CustomBeanTypeConverter extends DefaultTypeConverter {


    @Autowired
    private CustomBeanService customBeanService;

    @Transactional
    public Object convertValue(Map context, Object target, Member member, String propertyName, Object value, Class toType) {
        if (CustomBean.class.isAssignableFrom(toType)) {
            String _value = StringUtil.nullValue(ClassUtil.isArray(value) ? Array.get(value, 0) : value);
            if(StringUtil.isBlank(_value)){
                return Array.newInstance(toType.getComponentType(),0);
            }
            return customBeanService.get(Long.valueOf(_value));
        } else if (CustomBean[].class.isAssignableFrom(toType)) {
            String _value = StringUtil.nullValue(ClassUtil.isArray(value) ? Array.get(value, 0) : value);
            if(StringUtil.isBlank(_value)){
                return Array.newInstance(toType.getComponentType(),0);
            }
            String[] ids = _value.split(",");
            List<CustomBean> customBeanList = new ArrayList<CustomBean>();
            for (String id : ids) {
                customBeanList.add(customBeanService.get(Long.valueOf(id)));
            }
            return customBeanList.toArray((CustomBean[]) Array.newInstance(toType.getComponentType(), ((ArrayList) customBeanList).size()));
        } else if (value instanceof CustomBean && toType == String.class) {
            CustomBean customBean = (CustomBean) value;
            customBeanService.save(customBean);
            return customBean.getId().toString();
        } else if (value instanceof CustomBean[] && toType == String.class) {
            StringBuilder stringBuilder = new StringBuilder();
            for (CustomBean customBean : (CustomBean[]) value) {
                customBeanService.save(customBean);
                stringBuilder.append(customBean.getId()).append(",");
            }
            return stringBuilder.toString();
        }
        return super.convertValue(context, target, member, propertyName, value, toType);
    }

}
