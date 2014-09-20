package com.fantasy.framework.util.asm;

import com.fantasy.attr.bean.AttributeType;
import com.fantasy.attr.bean.AttributeValue;
import com.fantasy.attr.bean.AttributeVersion;
import com.fantasy.attr.util.VersionUtil;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.ognl.OgnlUtil;
import org.junit.Test;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.io.IOException;
import java.util.Arrays;

public class AsmUtilTest implements Opcodes {

    @Test
    public void asmTest(){
        Article article = new Article();
        System.out.println(ClassUtil.forName("int"));
        System.out.println(Type.getDescriptor(Long.class));
    }

    @Test
    public void test() throws IOException, InterruptedException, ClassNotFoundException, IllegalAccessException, InstantiationException {

        Article article = new Article();

        AttributeVersion version = new AttributeVersion();
        version.setNumber("temp");
        version.setClassName(Article.class.getName());

        AttributeType attributeType = new AttributeType();
        attributeType.setName("测试数据类型");
        attributeType.setDataType(String.class.getName());

        AttributeType attributeType1 = new AttributeType();
        attributeType1.setName("测试数据类型");
        attributeType1.setDataType(Integer.class.getName());

        com.fantasy.attr.bean.Attribute attribute = new com.fantasy.attr.bean.Attribute();
        attribute.setCode("test");
        attribute.setName("测试字段");
        attribute.setAttributeType(attributeType);

        com.fantasy.attr.bean.Attribute attribute1 = new com.fantasy.attr.bean.Attribute();
        attribute1.setCode("testInt");
        attribute1.setName("测试字段");
        attribute1.setAttributeType(attributeType1);

        version.setAttributes(Arrays.asList(attribute,attribute1));

        Class clzz = VersionUtil.makeClass(version);

        System.out.println(AsmUtil.trace(Article.class));

//        System.out.println(AsmUtil.trace(clzz));


        for(java.lang.reflect.Method method : clzz.getDeclaredMethods()){
            System.out.println(method.toString());
        }

        Article o = (Article)clzz.newInstance();

        AttributeValue attributeValue = new AttributeValue();
        attributeValue.setAttribute(attribute);
        attributeValue.setVersion(version);
        attributeValue.setValue("test");

        AttributeValue attributeValue1 = new AttributeValue();
        attributeValue1.setAttribute(attribute1);
        attributeValue1.setVersion(version);
        attributeValue1.setValue("12");

        o.setAttributeValues(Arrays.asList(attributeValue,attributeValue1));

        System.out.println(o);

        System.out.println(OgnlUtil.getInstance().getValue("test",o));

        System.out.println(OgnlUtil.getInstance().getValue("testInt",o));

        System.out.println(OgnlUtil.getInstance().getValue("testInt",o));

//       System.out.println(AsmUtil.trace(Article.class));

    }

}
