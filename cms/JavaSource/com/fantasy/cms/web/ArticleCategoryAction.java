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
        this.attrs.put("category",this.cmsService.save(category));
        return SUCCESS;
    }

    public String search(Pager<ArticleCategory> pager,List<PropertyFilter> filters){
        this.attrs.put(ROOT,this.cmsService.findCategoryPager(pager, filters));
        return SUCCESS;
    }

    public String view(String id) {
        this.attrs.put(ROOT, cmsService.get(id));
        return SUCCESS;
    }

    public String delete(String... id){
        for (String code : id) {
            this.cmsService.delete(code);
        }
        return SUCCESS;
    }

    public String update(ArticleCategory category) {
        this.attrs.put("category",this.cmsService.save(category));
        return SUCCESS;
    }

}
