package org.jfantasy.build;

import org.jfantasy.test.bean.Article;
import org.jfantasy.test.bean.ArticleCategory;
import org.jfantasy.test.bean.Content;

import java.util.ArrayList;
import java.util.UUID;

public class TestDataBuilder {

    public static <T> T build(Class<T> clazz, String keywords) {
        if(ArticleCategory.class.isAssignableFrom(clazz)){
            ArticleCategory category = new ArticleCategory();
            category.setCode("test");
            category.setName("测试");
            category.setLayer(0);
            category.setArticles(new ArrayList<Article>());
            return (T)category;
        }else if(Article.class.isAssignableFrom(clazz)){
            Article article = new Article();
            article.setCategory(build(ArticleCategory.class,keywords));
            article.setAuthor(keywords);
            article.setTitle(keywords + " 测试");
            article.setSummary(keywords + " Summary UUID = " + UUID.randomUUID());
            article.setContent(new Content(keywords + " Content"));
            article.getCategory().getArticles().add(article);
            return (T) article;
        }
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace(System.err);
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace(System.err);
            return null;
        }
    }

}
