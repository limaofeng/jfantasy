package com.fantasy.swp;

import java.util.List;

public interface DataAnalyzer {

    /**
     * 获取数据对象
     *
     * @param value     字符串形式的value
     * @param clazz     java类型
     * @param list      是否为集合
     * @param arguments 调用参数
     * @param <T>       泛型
     * @return object
     */
    public <T> Object exec(String value, Class<T> clazz, boolean list, List arguments);

}
