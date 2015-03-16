package com.fantasy.swp.factory;

import com.fantasy.swp.ISwpWebsite;
import com.fantasy.swp.service.PageBeanService;
import com.fantasy.swp.service.TemplateBeanService;
import com.fantasy.system.bean.Website;
import com.fantasy.system.service.WebsiteService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 站点工厂
 */
@Component
public class SwpWebsiteFactory {

    @Resource
    private WebsiteService websiteService;
    @Resource
    private TemplateBeanService templateBeanService;
    @Resource
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
