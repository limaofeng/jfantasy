package org.jfantasy.springboot.rest;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.springboot.bean.Article;
import org.jfantasy.springboot.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    @RequestMapping("/")
    String home() {
        return "Hello World!";
    }

    @Autowired
    private ArticleService articleService;

    @RequestMapping(value = "/jpaarticles", method = RequestMethod.GET)
    public Iterable<Article> jpaarticles() {
        return this.articleService.findAll();
    }

    @RequestMapping(value = "/articles", method = RequestMethod.POST)
    public Article save(@RequestBody Article article) {
        return this.articleService.save(article);
    }

    @RequestMapping(value = "/articles", method = RequestMethod.GET)
    public Pager<Article> articles(Pager<Article> pager, List<PropertyFilter> filters) {
        return this.articleService.findPager(pager, filters);
    }

}
