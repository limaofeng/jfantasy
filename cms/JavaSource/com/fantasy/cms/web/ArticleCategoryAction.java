package com.fantasy.cms.web;

import com.fantasy.cms.bean.ArticleCategory;
import com.fantasy.cms.service.CmsService;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ArticleCategoryAction extends ActionSupport {

    @Autowired
    private CmsService cmsService;

    public String create(ArticleCategory category){
        System.out.println(category);
        return SUCCESS;
    }

    public String search(Pager<ArticleCategory> pager,List<PropertyFilter> filters){
        this.attrs.put(ROOT,this.cmsService.findCategoryPager(pager,filters));
        return SUCCESS;
    }

    public String view(String id) {
        this.attrs.put(ROOT, cmsService.get(id));
        return SUCCESS;
    }

    public String delete(String... id){
        this.cmsService.delete(id);
        return SUCCESS;
    }

    public String update(ArticleCategory category) {
        System.out.println(category);
        return SUCCESS;
    }

}
