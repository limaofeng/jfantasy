package org.jfantasy.website.factory;

import org.jfantasy.website.Template;

/**
 * 模板工厂
 */
public interface TemplateFactory {

    public Template getTemplate(String path);

}
