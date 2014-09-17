package com.fantasy.swp.analysis.data;

import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.swp.DataAnalyzer;

import java.util.List;

public class BasicTypeAnalyzer implements DataAnalyzer {

    @Override
    public <T> Object exec(String value, Class<T> clazz, boolean list, List arguments) {
        if (ClassUtil.isBasicType(clazz)) {
            return ClassUtil.newInstance(clazz, value);
        }
        if (clazz.isAssignableFrom(String.class)) {
            return value;
        }
        throw new RuntimeException("不支持的数据类型:" + clazz);
    }

}