package com.fantasy.attr.storage.service;

import com.fantasy.attr.framework.converter.CustomBeanTypeConverter;
import com.fantasy.attr.framework.util.VersionUtil;
import com.fantasy.attr.storage.bean.*;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.ognl.OgnlUtil;
import junit.framework.Assert;
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
    private AttributeService attributeService;
    @Autowired
    private CustomBeanDefinitionService customBeanDefinitionService;
    private List<Attribute> attributes = new ArrayList<Attribute>();
    private String className = "org.jfantasy.test.UserDel";

    @Before
    public void setUp() throws Exception{
        //自定义bean
        //定义bean className 添加属性
        AttributeType attributeType = attributeTypeService.findUniqueByJavaType(String.class);
        Attribute attribute = new Attribute();
        attribute.setCode("number");
        attribute.setName("String字段");
        attribute.setDescription("test");
        attribute.setAttributeType(attributeType);
        attribute.setNonNull(true);
        attribute.setNotTemporary(false);
        attributeService.save(attribute);
        attributes.add(attribute);
        customBeanDefinitionService.save(className, "测试", attributes);

        //动态bean
        //1.定义扩展版本 添加属性  attrType = L className   attr = xxx
        //定义转换器
        Converter converter = new Converter();
        converter.setName("动态bean转换器");
        converter.setTypeConverter(CustomBeanTypeConverter.class.getName());
        converter.setDescription("");
        converterService.save(converter);

        converter = converterService.findUnique(Restrictions.eq("name", "动态bean转换器"), Restrictions.eq("typeConverter", CustomBeanTypeConverter.class.getName()));
        logger.debug(converter);
        Assert.assertNotNull(converter);

        //定义数据类型
        AttributeType attributeType1 = new AttributeType();
        attributeType1.setName("UserDel");
        attributeType1.setDataType("org.jfantasy.test.UserDel");
        attributeType1.setConverter(converter);
        attributeType1.setDescription("UserDel");
        attributeTypeService.save(attributeType1);

        attributeType = attributeTypeService.findUnique(Restrictions.eq("name", "UserDel"));
        logger.debug(attributeType1);
        Assert.assertNotNull(attributeType1);

        //定义属性
        Attribute attribute1 = new Attribute();
        attribute1.setCode("moneys");
        attribute1.setName("UserDelBean");
        attribute1.setDescription("");
        attribute1.setAttributeType(attributeType);
        attribute1.setNonNull(true);
        attribute1.setNotTemporary(false);
        attributeService.save(attribute1);

        attributeVersionService.save(Article.class.getName(),"fantasy_article_v1",attribute1);
    }


    @After
    public void tearDown() throws Exception {
        this.testDelete();
    }

    public void testDelete(){
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_title","测试动态bean"));
        List<Article> articles = this.articleService.find(filters,"id","asc",10);
        for(Article article : articles){
            this.articleService.delete(article.getId());
        }
        this.customBeanDefinitionService.delete("org.jfantasy.test.UserDel");

        for (Attribute attribute : attributes) {
            this.attributeService.delete(attribute.getId());
        }

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
