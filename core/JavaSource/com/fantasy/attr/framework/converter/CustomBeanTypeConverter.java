package com.fantasy.attr.framework.converter;

import com.fantasy.attr.framework.CustomBean;
import com.fantasy.attr.storage.service.CustomBeanService;
import ognl.DefaultTypeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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
            return customBeanService.get(Long.valueOf(value.toString()));
        } else if (CustomBean[].class.isAssignableFrom(toType)) {
            String[] ids = value.toString().split(",");
            List<CustomBean> customBeanList = new ArrayList<CustomBean>();
            for (String id : ids) {
                customBeanList.add(customBeanService.get(Long.valueOf(id)));
            }
            return customBeanList.toArray(new CustomBean[customBeanList.size()]);
        } else if (value instanceof CustomBean && toType == String.class) {
            CustomBean customBean = (CustomBean) value;
            customBeanService.save(customBean);
            return customBean.getId();
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
