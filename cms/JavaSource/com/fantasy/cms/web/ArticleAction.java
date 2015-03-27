package com.fantasy.cms.web;

import com.fantasy.cms.service.CmsService;
import com.fantasy.framework.struts2.ActionSupport;
import org.springframework.beans.factory.annotation.Autowired;


public class ArticleAction extends ActionSupport {

    @Autowired
    private CmsService cmsService;

    public String view(Long id) {
//        this.attrs.put(ROOT, cmsService.get(id));
        System.out.println(id);
        return SUCCESS;
    }

}
