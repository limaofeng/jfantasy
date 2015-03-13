package com.fantasy.framework.util.asm;

import com.fantasy.attr.storage.bean.AttributeType;
import com.fantasy.attr.storage.bean.AttributeVersion;
import com.fantasy.attr.framework.util.VersionUtil;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.ognl.OgnlUtil;
import junit.framework.Assert;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.objectweb.asm.Opcodes;

import java.io.IOException;
import java.util.Arrays;

public class AsmUtilTest implements Opcodes {

    private final static Log logger = LogFactory.getLog(AsmUtilTest.class);

    @Test
    public void testSetBoolean(){
        //测试 boolean 类型
        logger.debug("\n---------测试 boolean 类型---------");

//        logger.debug(AsmUtil.trace(Article.class));

        Class newClass = AsmUtil.makeClass(Article.class.getName() + "$boolean",Article.class.getName(),new Property("flag",boolean.class));
        Assert.assertNotNull(newClass);

//        logger.debug(AsmUtil.trace(newClass));

        Object newObject = ClassUtil.newInstance(newClass);
        Assert.assertNotNull(newObject);

        OgnlUtil.getInstance().setValue("flag", newObject, ConvertUtils.convert("false", boolean.class));

        logger.debug(newObject);
        logger.debug("flag type = " + ClassUtil.getDeclaredField(newClass,"flag").getType());
        logger.debug("flag = " + OgnlUtil.getInstance().getValue("flag",newObject));

        Assert.assertEquals(false,OgnlUtil.getInstance().getValue("flag",newObject));

        //测试 Boolean 类型
        logger.debug("\n---------测试 Boolean 类型---------");
        newClass = AsmUtil.makeClass(Article.class.getName() + "$Lboolean",Article.class.getName(),new Property("flag",Boolean.class));
        Assert.assertNotNull(newClass);

        newObject = ClassUtil.newInstance(newClass);
        Assert.assertNotNull(newObject);

        OgnlUtil.getInstance().setValue("flag", newObject, ConvertUtils.convert("false", boolean.class));

        logger.debug(newObject);
        logger.debug("flag type = " + ClassUtil.getDeclaredField(newClass,"flag").getType());
        logger.debug("flag = " + OgnlUtil.getInstance().getValue("flag",newObject));

        Assert.assertEquals(false,OgnlUtil.getInstance().getValue("flag",newObject));



    }

    @Test
    public void test() throws IOException, InterruptedException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        AttributeVersion version = new AttributeVersion();
        version.setNumber("temp");
        version.setClassName(Article.class.getName());

        AttributeType attributeType = new AttributeType();
        attributeType.setName("测试数据类型");
        attributeType.setDataType(String.class.getName());

        AttributeType attributeType1 = new AttributeType();
        attributeType1.setName("测试数据类型");
        attributeType1.setDataType(Integer.class.getName());

        com.fantasy.attr.storage.bean.Attribute attribute = new com.fantasy.attr.storage.bean.Attribute();
        attribute.setCode("test");
        attribute.setName("测试字段");
        attribute.setAttributeType(attributeType);

        com.fantasy.attr.storage.bean.Attribute attribute1 = new com.fantasy.attr.storage.bean.Attribute();
        attribute1.setCode("testInt");
        attribute1.setName("测试字段");
        attribute1.setAttributeType(attributeType1);

        version.setAttributes(Arrays.asList(attribute,attribute1));

        Class clzz = VersionUtil.makeClass(version);

//        logger.debug(AsmUtil.trace(Article.class));

        logger.debug(" println method");
        for(java.lang.reflect.Method method : clzz.getDeclaredMethods()){
            logger.debug(method.toString());
        }

        Article o = (Article)clzz.newInstance();

        OgnlUtil.getInstance().setValue(attribute.getCode(),o,"test");

        OgnlUtil.getInstance().setValue(attribute1.getCode(),o,"123");

        Assert.assertEquals(OgnlUtil.getInstance().getValue("test",o),"test");

        Assert.assertEquals(123,OgnlUtil.getInstance().getValue("testInt",o));

    }

}
