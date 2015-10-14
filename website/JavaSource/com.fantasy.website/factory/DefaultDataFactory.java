package com.fantasy.website.factory;

import com.fantasy.website.TemplateData;

import java.math.BigDecimal;

/**
 * 默认实现
 */
public class DefaultDataFactory implements DataFactory {

    @Override
    public TemplateData getStringData(String value) {
        return null;
    }

    @Override
    public TemplateData getTemplateData(Integer value) {
        return null;
    }

    @Override
    public TemplateData getTemplateData(BigDecimal value) {
        return null;
    }

    @Override
    public TemplateData getJSONData(String json) {
        return null;
    }

    @Override
    public TemplateData getHqlData(String hql) {
        return null;
    }

}
