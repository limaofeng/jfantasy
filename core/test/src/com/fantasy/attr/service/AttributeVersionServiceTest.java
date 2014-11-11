package com.fantasy.attr.service;

import com.fantasy.attr.bean.*;
import com.fantasy.attr.typeConverter.PrimitiveTypeConverter;
import com.fantasy.attr.typeConverter.UserTypeConverter;
import com.fantasy.attr.util.VersionUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.ognl.OgnlUtil;
import com.fantasy.security.bean.User;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class AttributeVersionServiceTest {

    private final static Log logger = LogFactory.getLog(AttributeVersionServiceTest.class);

    @Resource
    private AttributeVersionService attributeVersionService;
    @Resource
    private ArticleService articleService;
    @Resource
    private ConverterService converterService;
    @Resource
    private AttributeTypeService attributeTypeService;
    @Resource
    private AttributeService attributeService;

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
        attribute.setDescription("");
        attribute.setAttributeType(attributeType);
        attribute.setNonNull(true);
        attribute.setNotTemporary(false);
        attributeService.save(attribute);

        Converter userConverter = new Converter();
        userConverter.setName("用户对象转换器");
        userConverter.setTypeConverter(UserTypeConverter.class.getName());
        userConverter.setDescription("");
        converterService.save(userConverter);

        AttributeType userAttributeType = new AttributeType();
        userAttributeType.setName("用户类型");
        userAttributeType.setDataType(User.class.getName());
        userAttributeType.setConverter(userConverter);
        userAttributeType.setDescription("");
        attributeTypeService.save(userAttributeType);

        Attribute userAttribute = new Attribute();
        userAttribute.setCode("user");
        userAttribute.setName("测试Int类型字段");
        userAttribute.setDescription("");
        userAttribute.setAttributeType(userAttributeType);
        userAttribute.setNonNull(true);
        userAttribute.setNotTemporary(false);
        attributeService.save(userAttribute);

        AttributeVersion version = new AttributeVersion();
        version.setNumber("1.0");
        version.setClassName(Article.class.getName());
        version.setAttributes(new ArrayList<Attribute>());
        version.getAttributes().add(attribute);
        version.getAttributes().add(userAttribute);
        attributeVersionService.save(version);

    }

    @After
    public void tearDown() throws Exception {
        AttributeVersion version = attributeVersionService.getVersion(Article.class, "1.0");
        if (version == null) {
            return;
        }
        for (Attribute attribute : version.getAttributes()) {
            this.converterService.delete(attribute.getAttributeType().getConverter().getId());
        }
        this.attributeVersionService.delete(version.getId());
    }

    //    @Test
    public void testSearch() throws Exception {

    }

    //    @Test
    public void testFindPager() throws Exception {

    }

    @Test
    public void testSave() throws Exception {

        Article article = VersionUtil.createDynaBean(Article.class, "1.0");
        article.setTitle("测试数据标题");
        article.setSummary("测试数据摘要");

        AttributeType attributeType = ObjectUtil.find(article.getVersion().getAttributes(),"code","intTest").getAttributeType();
        AttributeType userAttributeType = ObjectUtil.find(article.getVersion().getAttributes(),"code","user").getAttributeType();

        VersionUtil.getOgnlUtil(attributeType).setValue("intTest", article, "456");

        logger.debug(article);

        Assert.assertEquals(456, OgnlUtil.getInstance().getValue("intTest", article));

        this.articleService.save(article);

        article = this.articleService.get(article.getId());

        logger.debug(article);

        Assert.assertEquals(456, OgnlUtil.getInstance().getValue("intTest", article));

        VersionUtil.getOgnlUtil(attributeType).setValue("intTest", article, "123");

        VersionUtil.getOgnlUtil(userAttributeType).setValue("user", article, "admin");

        logger.debug(VersionUtil.getOgnlUtil(userAttributeType).getValue("user", article));

        Assert.assertEquals(123, OgnlUtil.getInstance().getValue("intTest", article));

        this.articleService.save(article);

        logger.debug(article);

        Assert.assertEquals(123, OgnlUtil.getInstance().getValue("intTest", article));

        for(Article art : this.articleService.find(Restrictions.eq("title", "测试数据标题"))){
            this.articleService.delete(art.getId());
        }

    }

    //    @Test
    public void testGet() throws Exception {

    }

    //    @Test
    public void testDelete() throws Exception {

    }

    //    @Test
    public void testGetVersion() throws Exception {

    }
}