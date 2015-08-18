package com.fantasy.website.factory;

import com.fantasy.website.Template;

/**
 * 模板工厂
 */
public interface TemplateFactory {

    public Template getTemplate(String path);

}
