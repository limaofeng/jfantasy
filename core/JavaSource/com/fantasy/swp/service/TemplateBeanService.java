package com.fantasy.swp.service;


import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.swp.ITemplage;
import com.fantasy.swp.bean.DataInferface;
import com.fantasy.swp.bean.Template;
import com.fantasy.swp.bean.TemplateBean;
import com.fantasy.swp.bean.enums.PageType;
import com.fantasy.system.bean.Website;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TemplateBeanService {
    @Resource
    private TemplateService templateService;
    @Resource
    private DataInferfaceService dataInferfaceService;

    public List<ITemplage> listTemplate() {
        List<Template> templates = this.templateService.find(new ArrayList<PropertyFilter>());
        List<ITemplage> templateBeans = new ArrayList<ITemplage>();
        for(Template template : templates){
            TemplateBean bean = new TemplateBean();
            bean.setTemplate(template);
            templateBeans.add(bean);
        }
        return templateBeans;
    }

    public ITemplage get(String path, Long websiteId){
        Template template = templateService.findUniqueByPath(path,websiteId);
        TemplateBean bean = new TemplateBean();
        bean.setTemplate(template);
        return bean;
    }

    public void remove(String path, Long websiteId) {
        this.templateService.deleteByPath(path,websiteId);
    }

    public void saveTemplate(Website website, String path, String html, PageType pageType) {
        this.saveTemplate(website, path, html, pageType,null,null);
    }

    public void saveTemplate(Website website, String path, String html, PageType pageType, List<DataInferface> dataInferfaces) {
        this.saveTemplate(website, path, html, pageType,dataInferfaces,null);
    }

    public void saveTemplate(Website website, String path, String html, PageType pageType, List<DataInferface> dataInferfaces, String dataKey) {
        Template template = new Template();
        template.setPath(path);
        template.setContent(html);
        template.setWebSite(website);
        template.setPageType(pageType);
        template.setDataInferfaces(dataInferfaces);
        template.setDataKey(dataKey);
        this.templateService.save(template);

        if(dataInferfaces!=null && dataInferfaces.size()>0){
            for(DataInferface inferface : dataInferfaces){
                inferface.setTemplate(template);
                dataInferfaceService.save(inferface);
            }
        }
    }
}
