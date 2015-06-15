package com.fantasy.attr;

import com.fantasy.attr.framework.CustomBean;
import com.fantasy.attr.framework.util.AttributeUtils;
import com.fantasy.attr.storage.bean.CustomBeanDefinition;
import com.fantasy.attr.storage.service.CustomBeanDefinitionService;
import com.fantasy.attr.storage.service.CustomBeanService;
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
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class CustomBeanServiceTest {

    private final static Log LOG = LogFactory.getLog(CustomBeanServiceTest.class);

    @Autowired
    private CustomBeanDefinitionService customBeanDefinitionService;
    @Autowired
    private CustomBeanService customBeanService;

    private String className = "org.jfantasy.test.CustomBean";

    @Before
    public void setUp() throws Exception {
        this.tearDown();

        customBeanDefinitionService.save(className, "测试", AttributeUtils.integer("number","数字字段","test"), AttributeUtils.strings("strs", "数组字符串", "test"));
    }

    @After
    public void tearDown() throws Exception {
        this.customBeanDefinitionService.delete(className);

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

        object = customBeanService.get(id);

        Assert.assertEquals(1, OgnlUtil.getInstance().getValue("number", object));

        OgnlUtil.getInstance().setValue("number", object, "2");

        customBeanService.save(object);

        Assert.assertEquals(id, object.getId());

        Assert.assertEquals(2, OgnlUtil.getInstance().getValue("number", object));

        object = customBeanService.get(id);

        Assert.assertEquals(id, object.getId());

        Assert.assertEquals(2, OgnlUtil.getInstance().getValue("number", object));

        OgnlUtil.getInstance().setValue("strs[0]", object, "limaofeng");
        OgnlUtil.getInstance().setValue("strs[1]", object, "huangli");

        customBeanService.save(object);

        object = customBeanService.get(id);

        LOG.debug(object);

    }

}