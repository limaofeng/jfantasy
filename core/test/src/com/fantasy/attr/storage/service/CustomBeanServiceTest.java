package com.fantasy.attr.storage.service;

import com.fantasy.attr.framework.CustomBean;
import com.fantasy.attr.storage.bean.Attribute;
import com.fantasy.attr.storage.bean.AttributeType;
import com.fantasy.attr.storage.bean.CustomBeanDefinition;
import com.fantasy.framework.util.common.ClassUtil;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class CustomBeanServiceTest {

    private final static Log LOG = LogFactory.getLog(CustomBeanServiceTest.class);

    @Autowired
    private AttributeService attributeService;
    @Autowired
    private AttributeTypeService attributeTypeService;
    @Autowired
    private CustomBeanDefinitionService customBeanDefinitionService;
    @Autowired
    private CustomBeanService customBeanService;

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

        customBeanDefinitionService.save(className, "测试", attributes);
    }

    @After
    public void tearDown() throws Exception {
        this.customBeanDefinitionService.delete(className);

        for (Attribute attribute : attributes) {
            this.attributeService.delete(attribute.getId());
        }

        for (CustomBeanDefinition definition : this.customBeanDefinitionService.find(Restrictions.isNull("className"))) {
            this.customBeanDefinitionService.delete(definition.getId());
        }

    }

    @Test
    public void save() {
        CustomBean object = ClassUtil.newInstance(className, CustomBean.class);
        OgnlUtil.getInstance().setValue("number", object, "1");
        LOG.debug(object);
        customBeanService.save(object);

        Long id = object.getId();

        LOG.debug("保存数据的ID:" + id);

        Assert.assertNotNull(object.getId());

        Assert.assertEquals(1, OgnlUtil.getInstance().getValue("number", object));

        OgnlUtil.getInstance().setValue("number", object, "2");

        customBeanService.save(object);

        Assert.assertEquals(id, object.getId());

        Assert.assertEquals(2, OgnlUtil.getInstance().getValue("number", object));

        object = customBeanService.get(id);

        Assert.assertEquals(id, object.getId());

        Assert.assertEquals(2, OgnlUtil.getInstance().getValue("number", object));

    }

}