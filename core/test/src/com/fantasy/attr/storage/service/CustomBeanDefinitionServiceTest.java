package com.fantasy.attr.storage.service;

import com.fantasy.attr.storage.bean.Attribute;
import com.fantasy.attr.storage.bean.AttributeType;
import com.fantasy.attr.storage.bean.CustomBeanDefinition;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.framework.util.ognl.OgnlUtil;
import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
public class CustomBeanDefinitionServiceTest {

    private final static Log LOG = LogFactory.getLog(AttributeVersionServiceTest.class);

    @Resource
    private AttributeService attributeService;
    @Resource
    private ConverterService converterService;
    @Resource
    private AttributeTypeService attributeTypeService;
    @Resource
    private CustomBeanDefinitionService customBeanDefinitionService;

    private List<Attribute> attributes = new ArrayList<Attribute>();

    private String className = "org.jfantasy.test.UserDel";

    @Before
    public void setUp() throws Exception {
        this.tearDown();

        AttributeType attributeType = attributeTypeService.findUniqueByJavaType(Integer.class);

        Attribute attribute = new Attribute();
        attribute.setCode("number");
        attribute.setName("数字字段");
        attribute.setDescription("test");
        attribute.setAttributeType(attributeType);
        attribute.setNonNull(true);
        attribute.setNotTemporary(false);
        attributeService.save(attribute);

        attributes.add(attribute);
    }

    @After
    public void tearDown() throws Exception {
        this.customBeanDefinitionService.delete(className);

        for(Attribute attribute : attributes){
            this.attributeService.delete(attribute.getId());
        }

    }

    @Test
    public void testSave() throws Exception {
        CustomBeanDefinition definition = customBeanDefinitionService.save(className, "测试", attributes);

        Assert.assertNotNull(definition);

        Class clazz = ClassUtil.forName(className);

        Assert.assertNotNull(clazz);

        Object o = ClassUtil.newInstance(clazz);

        LOG.debug(o);

        OgnlUtil.getInstance().setValue("number", o, "10");

        Assert.assertNotNull(o);

        LOG.debug(JSON.set("text").serialize(o));

    }

}