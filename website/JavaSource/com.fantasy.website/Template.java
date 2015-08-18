package com.fantasy.website;

import java.util.Map;

/**
 * 页面模板
 */
public interface Template<T> {

    /**
     * 获取模板内容
     *
     * @return T
     */
    public T getContent();

    /**
     * 模板数据
     *
     * @return Map<String,TemplateData>
     */
    public Map<String, TemplateData> getData();

}
