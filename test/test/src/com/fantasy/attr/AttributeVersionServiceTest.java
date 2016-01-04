package org.jfantasy.attr;

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Restrictions;
import org.jfantasy.attr.framework.CustomBeanFactory;
import org.jfantasy.attr.framework.converter.PrimitiveTypeConverter;
import org.jfantasy.attr.framework.converter.UserTypeConverter;
import org.jfantasy.attr.framework.util.AttributeUtils;
import org.jfantasy.attr.storage.bean.AttributeVersion;
import org.jfantasy.attr.storage.service.AttributeService;
import org.jfantasy.attr.storage.service.AttributeTypeService;
import org.jfantasy.attr.storage.service.AttributeVersionService;
import org.jfantasy.attr.storage.service.ConverterService;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.util.common.ClassUtil;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.jackson.JSON;
import org.jfantasy.framework.util.ognl.OgnlUtil;
import org.jfantasy.security.bean.User;
import org.jfantasy.security.service.UserService;
import org.jfantasy.test.bean.Article;
import org.jfantasy.test.service.CmsService;
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

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class AttributeVersionServiceTest {

    private final static Log logger = LogFactory.getLog(AttributeVersionServiceTest.class);

    @Autowired
    private AttributeVersionService attributeVersionService;
    @Autowired
    private CmsService cmsService;
    @Autowired
    private ConverterService converterService;
    @Autowired
    private AttributeTypeService attributeTypeService;
    @Autowired
    private AttributeService attributeService;
    @Autowired
    private UserService userService;
    @Autowired
    private CustomBeanFactory customBeanFactory;

    @Before
    public void setUp() throws Exception {
        //定义转换器
        //基本数据类型转换器
        converterService.save(PrimitiveTypeConverter.class, "测试转换器", "测试转换器");
        //用户类型转换器
        converterService.save(UserTypeConverter.class, "用户对象转换器", "用户对象转换器");
        //定义数据类型
        attributeTypeService.save(Integer.class, "Integer", "测试数据类", PrimitiveTypeConverter.class);
        attributeTypeService.save(User.class, "user", "user类型", UserTypeConverter.class);
        //定义版本添加属性
        attributeVersionService.save(Article.class.getName(), "1.0", AttributeUtils.bean("user", "user", "测试user", ClassUtil.forName(User.class.getName())), AttributeUtils.integer("intTest", "测试Int类型字段", "测试Int类型字段"));
    }

    @After
    public void tearDown() throws Exception {
        for(AttributeVersion version : this.attributeVersionService.getAttributeVersions()){
            try {
                this.attributeVersionService.delete(version.getId());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void testFindPager() throws Exception {
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQI_intTest", "123"));
        List<Article> articles = this.cmsService.findPager(new Pager<Article>(), filters).getPageItems();

        Assert.assertEquals(1, articles.size());

        Article article = articles.get(0);

        Assert.assertEquals(123, customBeanFactory.getOgnlUtil(ObjectUtil.find(article.getVersion().getAttributes(), "code", "intTest").getAttributeType()).getValue("intTest", article));

    }

    @Test
    public void testSave() throws Exception {
        Article article = customBeanFactory.makeDynaBean(Article.class, "1.0");
        article.setTitle("测试数据标题");
        article.setSummary("测试数据摘要");
        OgnlUtil.getInstance().setValue("intTest", article, "456");
        JSON.serialize(article);

        /*
        AttributeType attributeType = ObjectUtil.find(article.getVersion().getAttributes(), "code", "intTest").getAttributeType();
        AttributeType userAttributeType = ObjectUtil.find(article.getVersion().getAttributes(), "code", "user").getAttributeType();

        //测试普通数据类型
        customBeanFactory.getOgnlUtil(attributeType).setValue("intTest", article, "456");
        logger.debug(article);
        this.cmsService.save(article);
        article = this.cmsService.get(article.getId());
        logger.debug(article);
        Assert.assertEquals(456, OgnlUtil.getInstance().getValue("intTest", article));

        //修改普通数据类型
        customBeanFactory.getOgnlUtil(attributeType).setValue("intTest", article, "123");
        //测试对象数据类型
        customBeanFactory.getOgnlUtil(userAttributeType).setValue("user", article, "admin");
        logger.debug(customBeanFactory.getOgnlUtil(userAttributeType).getValue("user", article));
        Assert.assertEquals(123, OgnlUtil.getInstance().getValue("intTest", article));
        this.cmsService.save(article);
        logger.debug(article);
        Assert.assertEquals(123, OgnlUtil.getInstance().getValue("intTest", article));

        //测试 findPager 中的动态属性
        for (Article art : this.cmsService.findPager(new Pager<Article>(), new ArrayList<PropertyFilter>()).getPageItems()) {
            logger.debug(art);
            Assert.assertNotNull(OgnlUtil.getInstance().getValue("user", article));
            Assert.assertNotNull(OgnlUtil.getInstance().getValue("intTest", article));
        }

        this.testGet();

        this.testFindPager();

        this.testFind();

        for (Article art : this.cmsService.find(Restrictions.eq("title", "测试数据标题"))) {
            logger.debug(art);
        }
        */
    }

    private void testFind() {
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQI_intTest", "123"));
        for (Article art : this.cmsService.find(filters)) {
            logger.debug(art);
        }
        for (Article art : this.cmsService.find(Restrictions.eq("intTest", Integer.valueOf("123")))) {
            logger.debug(art);
        }
        /*
        TODO 动态字段 查询
        List<Article> articles = this.articleService.find(Restrictions.eq("user.username","admin"));
        Assert.assertEquals(1,articles.size());
        for(Article art : articles){
            logger.debug(art);
        }*/
    }

    public void testGet() throws Exception {
        Article article = this.cmsService.findUnique(Restrictions.eq("title", "测试数据标题"));

        Assert.assertNotNull(OgnlUtil.getInstance().getValue("user", article));
        Assert.assertNotNull(OgnlUtil.getInstance().getValue("intTest", article));

        article = this.cmsService.findUniqueBy("title", "测试数据标题");

        Assert.assertNotNull(OgnlUtil.getInstance().getValue("user", article));
        Assert.assertNotNull(OgnlUtil.getInstance().getValue("intTest", article));
    }

}