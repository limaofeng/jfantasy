package org.jfantasy.website.service;


import org.jfantasy.file.FileManager;
import org.jfantasy.file.bean.FileManagerConfig;
import org.jfantasy.file.service.FileManagerFactory;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.website.ITemplage;
import org.jfantasy.website.bean.*;
import org.jfantasy.website.bean.enums.PageType;
import org.jfantasy.system.bean.Website;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TemplateBeanService {
    @Autowired
    private TemplateService templateService;
    @Autowired
    private DataInferfaceService dataInferfaceService;
    @Autowired
    private FileManagerFactory fileManagerFactory;
    @Autowired
    private _PageService pageService;

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

    /**
     * 删除模板文件及所有page文件
     * @param path
     * @param website
     */
    public void removeFile(String path, Website website) {
        FileManager fileManager = this.fileManagerFactory.getFileManager(website.getDefaultFileManager().getId());
        Template template = this.templateService.findUniqueByPath(path,website.getId());
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQL_template.id",template.getId()+""));
        List<Page> pages = this.pageService.find(filters);
        // 删除page文件
        for(Page page : pages){
            List<PageItem> pageItems = page.getPageItems();
            for(PageItem pageItem : pageItems){
                fileManager.removeFile(pageItem.getFile());
            }
        }
        // 删除模板文件
        fileManager.removeFile(path);
    }
}
