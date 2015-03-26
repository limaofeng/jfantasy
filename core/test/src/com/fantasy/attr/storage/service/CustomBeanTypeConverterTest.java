package com.fantasy.attr.storage.service;

import com.fantasy.attr.framework.converter.CustomBeanTypeConverter;
import com.fantasy.attr.framework.util.AttributeUtils;
import com.fantasy.attr.framework.util.VersionUtil;
import com.fantasy.attr.storage.bean.Article;
import com.fantasy.attr.storage.bean.CustomBeanDefinition;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.ognl.OgnlUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Restrictions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hebo on 2015/3/23.
 * 动态bean转换测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class CustomBeanTypeConverterTest {

    private static Log logger = LogFactory.getLog(CustomBeanTypeConverterTest.class);

    @Autowired
    private ArticleService articleService;
    @Autowired
    private AttributeVersionService attributeVersionService;
    @Autowired
    private ConverterService converterService;
    @Autowired
    private AttributeTypeService attributeTypeService;
    @Autowired
    private CustomBeanDefinitionService customBeanDefinitionService;
    private String className = "org.jfantasy.test.UserDel";

    @Before
    public void setUp() throws Exception{
        customBeanDefinitionService.save(className, "测试", AttributeUtils.string("number","String字段","test"));

        //动态bean
        //1.定义扩展版本 添加属性  attrType = L className   attr = xxx
        //定义转换器
        converterService.save(CustomBeanTypeConverter.class, "动态bean转换器", "");

        attributeTypeService.save(ClassUtil.forName(className), "UserDel", "UserDel", CustomBeanTypeConverter.class);
        attributeTypeService.save(Array.newInstance(ClassUtil.forName(className)).getClass(), "UserDel", "UserDel", CustomBeanTypeConverter.class);

        attributeVersionService.save(Article.class.getName(),"fantasy_article_v1",AttributeUtils.bean("moneys","UserDelBean","", ClassUtil.forName(className)));
    }


    @After
    public void tearDown() throws Exception {
        this.testDelete();
    }

    public void testDelete(){
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_title", "测试动态bean"));
        List<Article> articles = this.articleService.find(filters,"id","asc",10);
        for(Article article : articles){
            this.articleService.delete(article.getId());
        }
        this.customBeanDefinitionService.delete(className);

        for (CustomBeanDefinition definition : this.customBeanDefinitionService.find(Restrictions.isNull("className"))) {
            this.customBeanDefinitionService.delete(definition.getId());
        }
    }


    @Test
    public void save(){
        Article article = VersionUtil.createDynaBean(Article.class, "fantasy_article_v1");
        article.setTitle("测试动态bean");
        article.setSummary("测试动态bean");
        OgnlUtil.getInstance().setValue("moneys.number",article,"123");
        this.articleService.save(article);
    }


}
