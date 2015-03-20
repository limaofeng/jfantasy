package com.fantasy.web;


import com.fantasy.cms.bean.Article;
import com.fantasy.framework.struts2.ActionSupport;

public class ArticleAction extends ActionSupport {

    public String index(){
        Article article = new Article();
        this.attrs.put(ROOT,new Article[]{article});
        return SUCCESS;
    }

    public String create(){
        return SUCCESS;
    }

    public String update(){
        return SUCCESS;
    }

    public String destroy(){
        return SUCCESS;
    }

    public String view(Long id){
        Article article = new Article();
        article.setId(id);
        this.attrs.put(ROOT,article);
        return SUCCESS;
    }

}
