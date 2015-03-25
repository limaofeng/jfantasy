package com.fantasy.web;


import com.fantasy.cms.bean.Article;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;

import java.util.List;

public class ArticleAction extends ActionSupport {

    public String search(Pager<Article> pager,List<PropertyFilter> filters){
        Article article = new Article();
        this.attrs.put(ROOT,new Article[]{article});
        return SUCCESS;
    }

    public String create(Article article){
        String title = article.getTitle();
        this.attrs.put(ROOT,title);
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
        Article article = new Article();
        article.setId(id);
        this.attrs.put(ROOT,article);
        return SUCCESS;
    }

}
