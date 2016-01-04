package org.jfantasy.website.bean;

import org.jfantasy.website.ITemplage;

public class TemplateBean implements ITemplage {

    private Template template;

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }
}
