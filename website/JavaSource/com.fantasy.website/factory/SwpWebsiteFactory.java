package com.fantasy.website.factory;

import com.fantasy.website.ISwpWebsite;
import com.fantasy.website.service.PageBeanService;
import com.fantasy.website.service.TemplateBeanService;
import com.fantasy.system.bean.Website;
import com.fantasy.system.service.WebsiteService;
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
