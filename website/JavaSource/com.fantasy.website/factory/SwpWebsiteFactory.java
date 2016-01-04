package org.jfantasy.website.factory;

import org.jfantasy.website.ISwpWebsite;
import org.jfantasy.website.service.PageBeanService;
import org.jfantasy.website.service.TemplateBeanService;
import org.jfantasy.system.bean.Website;
import org.jfantasy.system.service.WebsiteService;
import org.springframework.stereotype.Component;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 站点工厂
 */
@Component
public class SwpWebsiteFactory {

    @Autowired
    private WebsiteService websiteService;
    @Autowired
    private TemplateBeanService templateBeanService;
    @Autowired
    private PageBeanService pageBeanService;

    public ISwpWebsite getInstance(String code){
        Website website = websiteService.findUniqueByKey(code);
        SwpWebsite swpWebsite = new SwpWebsite();
        swpWebsite.setWebsite(website);
        swpWebsite.setTemplateBeanService(templateBeanService);
        swpWebsite.setPageBeanService(pageBeanService);

        return swpWebsite;
    }

}
