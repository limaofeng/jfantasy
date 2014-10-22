package com.test;

import com.fantasy.cms.bean.Article;
import com.fantasy.cms.service.CmsService;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.framework.util.ognl.OgnlUtil;

import javax.annotation.Resource;

public class AttributeAction extends ActionSupport{

    @Resource
    private CmsService cmsService;

    public String save(Article article) throws Exception {
        System.out.println(article.getVersion().getId());
        System.out.println(OgnlUtil.getInstance().getValue("test",article));
        cmsService.save(article);
        return SUCCESS;
    }

}