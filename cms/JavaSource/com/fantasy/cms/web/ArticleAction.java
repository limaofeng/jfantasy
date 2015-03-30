package com.fantasy.cms.web;

import com.fantasy.cms.bean.Article;
import com.fantasy.cms.service.CmsService;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.rest.RestActionSupport;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


public class ArticleAction extends RestActionSupport {

    @Autowired
    private CmsService cmsService;

    public String search(Pager<Article> pager,List<PropertyFilter> filters){
        this.attrs.put(ROOT,this.cmsService.findPager(pager,filters));
        return SUCCESS;
    }

    public String create(Article article){
        this.attrs.put(ROOT,article);
        return SUCCESS;
    }

    public String update(Article article){
        String title = article.getTitle();
        this.attrs.put(ROOT,title);
        return SUCCESS;
    }

    public String delete(Long id){
        Article article = new Article();
        article.setId(id);
        this.attrs.put(ROOT,article);
        return SUCCESS;
    }

    public String view(Long id){
        this.attrs.put(ROOT, this.cmsService.get(id));
        return SUCCESS;
    }

}
