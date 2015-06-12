package com.fantasy.cms.rest;

import com.fantasy.cms.bean.Article;
import com.fantasy.cms.service.CmsService;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private CmsService cmsService;

    public Pager<Article> search(Pager<Article> pager, List<PropertyFilter> filters) {

        return null;
    }

    @RequestMapping("/{id}")
    public Article view(@PathVariable("id") Long id) {
        return this.cmsService.get(id);
    }

    @RequestMapping(method = {RequestMethod.POST})
    public Article create(Article article) {
        this.cmsService.save(article);
        return article;
    }

    @RequestMapping(method = {RequestMethod.DELETE})
    public void delete(Long... id) {
        this.cmsService.delete(id);
    }

    @RequestMapping(method = {RequestMethod.PUT})
    public Article update(Article article) {
        this.cmsService.save(article);
        return article;
    }

}
