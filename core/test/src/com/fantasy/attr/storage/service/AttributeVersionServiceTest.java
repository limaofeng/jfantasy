package com.fantasy.attr.storage.service;

import com.fantasy.attr.storage.bean.*;
import com.fantasy.attr.framework.converter.PrimitiveTypeConverter;
import com.fantasy.attr.framework.converter.UserTypeConverter;
import com.fantasy.attr.framework.util.VersionUtil;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.ognl.OgnlUtil;
import com.fantasy.security.bean.User;
import com.fantasy.security.service.UserService;
import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Restrictions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class AttributeVersionServiceTest {

    private final static Log logger = LogFactory.getLog(AttributeVersionServiceTest.class);

    @Autowired
    private AttributeVersionService attributeVersionService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ConverterService converterService;
    @Autowired
    private AttributeTypeService attributeTypeService;
    @Autowired
    private AttributeService attributeService;
    @Autowired
    private UserService userService;

    @Before
    public void setUp() throws Exception {
        this.tearDown();

        Converter converter = new Converter();
        converter.setName("测试转换器");
        converter.setTypeConverter(PrimitiveTypeConverter.class.getName());
        converter.setDescription("");
        converterService.save(converter);

        converter = converterService.findUnique(Restrictions.eq("name", "测试转换器"), Restrictions.eq("typeConverter", PrimitiveTypeConverter.class.getName()));
        logger.debug(converter);
        Assert.assertNotNull(converter);

        AttributeType attributeType = new AttributeType();
        attributeType.setName("测试数据类");
        attributeType.setDataType(Integer.class.getName());
        attributeType.setConverter(converter);
        attributeType.setDescription("");
        attributeTypeService.save(attributeType);

        attributeType = attributeTypeService.findUnique(Restrictions.eq("name", "测试数据类"));
        logger.debug(attributeType);
        Assert.assertNotNull(attributeType);

        Attribute attribute = new Attribute();
        attribute.setCode("intTest");
        attribute.setName("测试Int类型字段");
        attribute.setDescription("test");
        attribute.setAttributeType(attributeType);
        attribute.setNonNull(true);
        attribute.setNotTemporary(false);
        attributeService.save(attribute);

        Converter userConverter = new Converter();
        userConverter.setName("用户对象转换器");
        userConverter.setTypeConverter(UserTypeConverter.class.getName());
        userConverter.setDescription("test");
        converterService.save(userConverter);

        AttributeType userAttributeType = new AttributeType();
        userAttributeType.setName("用户类型");
        userAttributeType.setDataType(User.class.getName());
        userAttributeType.setForeignKey("username");
        userAttributeType.setConverter(userConverter);
        userAttributeType.setDescription("");
        attributeTypeService.save(userAttributeType);

        Attribute userAttribute = new Attribute();
        userAttribute.setCode("user");
        userAttribute.setName("测试 user 类型字段");
        userAttribute.setDescription("");
        userAttribute.setAttributeType(userAttributeType);
        userAttribute.setNonNull(true);
        userAttribute.setNotTemporary(false);
        attributeService.save(userAttribute);

        attributeVersionService.save(Article.class.getName(),"1.0",attribute,userAttribute);

    }

    @After
    public void tearDown() throws Exception {
        for(Article art : this.articleService.find(Restrictions.eq("title", "测试数据标题"))){
            this.articleService.delete(art.getId());
        }

        for(Converter converter : converterService.find(Restrictions.eq("name", "测试转换器"))){
            this.converterService.delete(converter.getId());
        }

        AttributeVersion version = attributeVersionService.findUniqueByTargetClassName(Article.class.getName(), "1.0");
        if (version == null) {
            for(Converter converter : converterService.find(Restrictions.eq("description", "test"))){
                this.converterService.delete(converter.getId());
            }
            return;
        }
        for (Attribute attribute : version.getAttributes()) {
            this.converterService.delete(attribute.getAttributeType().getConverter().getId());
        }
        this.attributeVersionService.delete(version.getId());
    }

    public void testFindPager() throws Exception {
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQI_intTest","123"));
        List<Article> articles =  this.articleService.findPager(new Pager<Article>(),filters).getPageItems();

        Assert.assertEquals(1,articles.size());

        Article article = articles.get(0);

        Assert.assertEquals(123,VersionUtil.getOgnlUtil(ObjectUtil.find(article.getVersion().getAttributes(),"code","intTest").getAttributeType()).getValue("intTest",article));

    }

    @Test
    public void testSave() throws Exception {

        Article article = VersionUtil.createDynaBean(Article.class, "1.0");
        article.setTitle("测试数据标题");
        article.setSummary("测试数据摘要");

        AttributeType attributeType = ObjectUtil.find(article.getVersion().getAttributes(),"code","intTest").getAttributeType();
        AttributeType userAttributeType = ObjectUtil.find(article.getVersion().getAttributes(),"code","user").getAttributeType();

        //测试普通数据类型
        VersionUtil.getOgnlUtil(attributeType).setValue("intTest", article, "456");
        logger.debug(article);
        this.articleService.save(article);
        article = this.articleService.get(article.getId());
        logger.debug(article);
        Assert.assertEquals(456, OgnlUtil.getInstance().getValue("intTest", article));

        //修改普通数据类型
        VersionUtil.getOgnlUtil(attributeType).setValue("intTest", article, "123");
        //测试对象数据类型
        VersionUtil.getOgnlUtil(userAttributeType).setValue("user", article, "admin");
        logger.debug(VersionUtil.getOgnlUtil(userAttributeType).getValue("user", article));
        Assert.assertEquals(123, OgnlUtil.getInstance().getValue("intTest", article));
        this.articleService.save(article);
        logger.debug(article);
        Assert.assertEquals(123, OgnlUtil.getInstance().getValue("intTest", article));

        //测试 findPager 中的动态属性
        for(Article art : this.articleService.findPager(new Pager<Article>(),new ArrayList<PropertyFilter>()).getPageItems()){
            logger.debug(art);
            Assert.assertNotNull(OgnlUtil.getInstance().getValue("user", article));
            Assert.assertNotNull(OgnlUtil.getInstance().getValue("intTest", article));
        }

        this.testGet();

        this.testFindPager();

        this.testFind();

        for(Article art : this.articleService.find(Restrictions.eq("title", "测试数据标题"))){
            logger.debug(art);
        }

    }

    private void testFind() {
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQI_intTest", "123"));
        for(Article art : this.articleService.find(filters)){
            logger.debug(art);
        }
        for(Article art : this.articleService.find(Restrictions.eq("intTest",Integer.valueOf("123")))){
            logger.debug(art);
        }
        List<Article> articles = this.articleService.find(Restrictions.eq("user.username","admin"));
        Assert.assertEquals(1,articles.size());
        for(Article art : articles){
            logger.debug(art);
        }
    }

    public void testGet() throws Exception {
        Article article = this.articleService.findUnique(Restrictions.eq("title","测试数据标题"));

        Assert.assertNotNull(OgnlUtil.getInstance().getValue("user", article));
        Assert.assertNotNull(OgnlUtil.getInstance().getValue("intTest", article));

        article = this.articleService.findUniqueBy("title","测试数据标题");

        Assert.assertNotNull(OgnlUtil.getInstance().getValue("user", article));
        Assert.assertNotNull(OgnlUtil.getInstance().getValue("intTest", article));
    }

}