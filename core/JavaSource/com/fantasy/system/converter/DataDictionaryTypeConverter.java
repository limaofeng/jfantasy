package com.fantasy.system.converter;

import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.ognl.OgnlUtil;
import com.fantasy.framework.util.regexp.RegexpUtil;
import com.fantasy.system.bean.DataDictionary;
import com.fantasy.system.service.DataDictionaryService;
import ognl.DefaultTypeConverter;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import java.lang.reflect.Array;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用于将 DataDictionary 类作为动态属性时的转换方法。
 */
public class DataDictionaryTypeConverter extends DefaultTypeConverter {

    @Autowired
    private DataDictionaryService dataDictionaryService;

    @Transactional
    public Object convertValue(Map context, Object target, Member member, String propertyName, Object value, Class toType) {
        if (toType == DataDictionary.class) {
            String values = StringUtil.nullValue(ClassUtil.isArray(value) ? Array.get(value, 0) : value);
            return dataDictionaryService.get(values);
        } else if (toType == DataDictionary[].class) {
            String values = StringUtil.nullValue(ClassUtil.isArray(value) ? Array.get(value, 0) : value);
            if (StringUtil.isBlank(values)) {
                return new DataDictionary[0];
            }
            String[] datas = RegexpUtil.split(values, ",");
            if (datas.length == 0) {
                return new DataDictionary[0];
            }
            List<DataDictionary> dataDicts = new ArrayList<DataDictionary>();
            for (String data : datas) {
                DataDictionary dataDictionary = dataDictionaryService.get(data);
                if (dataDictionary == null) {
                    continue;
                }
                dataDicts.add(dataDictionary);
            }
            return dataDicts.toArray(new DataDictionary[dataDicts.size()]);
        } else if (value instanceof DataDictionary && toType == String.class) {
            return OgnlUtil.getInstance().getValue("key", value).toString();
        } else if (value instanceof DataDictionary[] && toType == String.class) {
            StringBuilder stringBuilder = new StringBuilder();
            for (DataDictionary dataDictionary : (DataDictionary[]) value) {
                stringBuilder.append(dataDictionary.getKey()).append(",");
            }
            return stringBuilder.toString();
        }
        return super.convertValue(context, target, member, propertyName, value, toType);
    }

}
