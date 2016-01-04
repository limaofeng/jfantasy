package org.jfantasy.attr;

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfantasy.attr.framework.util.AttributeUtils;
import org.jfantasy.attr.storage.bean.CustomBeanDefinition;
import org.jfantasy.attr.storage.service.AttributeVersionService;
import org.jfantasy.attr.storage.service.CustomBeanDefinitionService;
import org.jfantasy.framework.util.common.ClassUtil;
import org.jfantasy.framework.util.jackson.JSON;
import org.jfantasy.framework.util.ognl.OgnlUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class CustomBeanDefinitionServiceTest {

    private final static Log LOG = LogFactory.getLog(AttributeVersionServiceTest.class);

    @Autowired
    private CustomBeanDefinitionService customBeanDefinitionService;
    @Autowired
    private AttributeVersionService attributeVersionService;

    private String className = "org.jfantasy.test.TestCustomBean";

    @Before
    public void setUp() throws Exception {
        this.tearDown();
    }

    @After
    public void tearDown() throws Exception {
        this.customBeanDefinitionService.delete(className);
    }

    @Test
    public void testSave() throws Exception {
        CustomBeanDefinition definition = customBeanDefinitionService.save(className, "测试", AttributeUtils.integer("number", "数字字段", "test"), AttributeUtils.strings("strs", "数组字符串", "test"));

        Assert.assertNotNull(definition);

        Class clazz = ClassUtil.forName(className);

        Assert.assertNotNull(clazz);

        Object o = ClassUtil.newInstance(clazz);

        LOG.debug(o);

        OgnlUtil.getInstance().setValue("number", o, "10");

        Assert.assertNotNull(o);

        LOG.debug(JSON.serialize(o));

        OgnlUtil.getInstance().setValue("strs[0]", o, "limaofeng");
        OgnlUtil.getInstance().setValue("strs[1]", o, "huangli");

        LOG.debug(JSON.serialize(o));

    }

}