package com.fantasy.swp.factory;

import com.fantasy.swp.Template;

/**
 * 模板工厂
 */
public interface TemplateFactory {

    public Template getTemplate(String path);

}
