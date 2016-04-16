package org.jfantasy.springboot.rest.form;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.jfantasy.springboot.bean.Article;
import org.springframework.hateoas.ResourceSupport;

public class ArticleForm extends ResourceSupport {

    private Article article;

    @JsonCreator
    public ArticleForm(Article article) {//@JsonProperty("article")
        this.article = article;
    }

    public Article getArticle() {
        return article;
    }

}
