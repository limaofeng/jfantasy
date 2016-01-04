package org.jfantasy.attr;

import org.jfantasy.attr.framework.CustomBeanFactory;
import org.jfantasy.attr.framework.converter.CustomBeanTypeConverter;
import org.jfantasy.attr.framework.util.AttributeUtils;
import org.jfantasy.attr.storage.bean.CustomBeanDefinition;
import org.jfantasy.attr.storage.service.*;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.util.common.ClassUtil;
import org.jfantasy.framework.util.ognl.OgnlUtil;
import org.jfantasy.test.bean.Article;
import org.jfantasy.test.bean.ArticleCategory;
import org.jfantasy.test.service.CmsService;
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
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hebo on 2015/3/23.
 * 动态bean转换测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class CustomBeanTypeConverterTest {

    private static Log logger = LogFactory.getLog(CustomBeanTypeConverterTest.class);

    @Autowired
    private CmsService cmsService;
    @Autowired
    private AttributeVersionService attributeVersionService;
    @Autowired
    private ConverterService converterService;
    @Autowired
    private CustomBeanFactory customBeanFactory;
    @Autowired
    private AttributeService attributeService;
    @Autowired
    private AttributeTypeService attributeTypeService;
    @Autowired
    private CustomBeanDefinitionService customBeanDefinitionService;
    private String className = "org.jfantasy.test.UserDel";

    @Before
    public void setUp() throws Exception{
        //创建动态bean及字段
        customBeanDefinitionService.save(className, "测试", AttributeUtils.string("number","String字段","test"));
        //定义转换器
        converterService.save(CustomBeanTypeConverter.class, "动态bean转换器", "");
        //定义动态bean数据类型
            //单个对象
        attributeTypeService.save(ClassUtil.forName(className), "UserDel", "UserDel", CustomBeanTypeConverter.class);
            //数组对象
        //attributeTypeService.save(Array.newInstance(ClassUtil.forName(className)).getClass(), "UserDel", "UserDel", CustomBeanTypeConverter.class);
        //定义数据版本 添加属性
        attributeVersionService.save(Article.class.getName(),"fantasy_article_v1",AttributeUtils.bean("moneys","UserDelBean","", ClassUtil.forName(className)));
        if(cmsService.get("test") == null) {
            ArticleCategory category = new ArticleCategory();
            category.setCode("test");
            category.setName("用于测试的文章分类");
            cmsService.save(category);
        }
    }


    @After
    public void tearDown() throws Exception {
        this.testDelete();
    }

    public void testDelete(){
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_title", "测试动态bean"));
        List<Article> articles = this.cmsService.find(filters,"id","asc",10);
        for(Article article : articles){
            this.cmsService.delete(article.getId());
        }
        this.attributeVersionService.delete(attributeVersionService.findUniqueByTargetClassName(Article.class.getName(),"fantasy_article_v1").getId());
        this.customBeanDefinitionService.delete(className);
        this.attributeService.delete(attributeService.findByCode("moneys").getId());
        for (CustomBeanDefinition definition : this.customBeanDefinitionService.find(Restrictions.isNull("className"))) {
            this.customBeanDefinitionService.delete(definition.getId());
        }
    }


    @Test
    public void save(){
        Article article = customBeanFactory.makeDynaBean(Article.class, "fantasy_article_v1");
        article.setTitle("测试动态bean");
        article.setSummary("测试动态bean");
        article.setCategory(new ArticleCategory("test"));
        OgnlUtil.getInstance().setValue("moneys.number",article,"123");
        this.cmsService.save(article);
    }


}
