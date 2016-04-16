package org.jfantasy.springboot.rest;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.springboot.bean.Article;
import org.jfantasy.springboot.rest.form.ArticleForm;
import org.jfantasy.springboot.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.Callable;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;


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

    @RequestMapping(value="/response/annotation", method=RequestMethod.GET)
    public @ResponseBody Callable<String> responseBody() {

        return new Callable<String>() {
            public String call() throws Exception {
                // Do some work..
                Thread.sleep(3000L);
                return "The String ResponseBody";
            }
        };
    }

    @RequestMapping(value="/response/entity/headers", method=RequestMethod.GET)
    public Callable<ResponseEntity<String>> responseEntityCustomHeaders() {

        return new Callable<ResponseEntity<String>>() {
            public ResponseEntity<String> call() throws Exception {

                // Do some work..
                Thread.sleep(3000L);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.TEXT_PLAIN);
                return new ResponseEntity<String>("The String ResponseBody with custom header Content-Type=text/plain", headers, HttpStatus.OK);
            }
        };
    }

    @RequestMapping(value = "/articles/{id}", method = RequestMethod.GET)
    public HttpEntity<ArticleForm> greeting(@PathVariable("id") Long id) {
        ArticleForm greeting = new ArticleForm(articleService.get(id));
        greeting.add(linkTo(methodOn(TestController.class).greeting(id)).withSelfRel());
        return new ResponseEntity<ArticleForm>(greeting, HttpStatus.OK);
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
