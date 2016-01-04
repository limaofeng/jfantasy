package org.jfantasy.framework.util.common;

import org.jfantasy.framework.util.asm.Article;
import org.jfantasy.framework.util.reflect.Property;
import org.jfantasy.security.bean.User;
import org.jfantasy.security.bean.enums.Sex;
import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ClassUtilTest {

    private static Log LOG = LogFactory.getLog(ClassUtilTest.class);

    @Test
    public void testLoadClass() throws Exception {

    }

    @Test
    public void testGetBeanInfo() throws Exception {

    }

    @Test
    public void testNewInstance() throws Exception {

    }

    @Test
    public void testGetRealClass() throws Exception {
    }

    @Test
    public void testNewInstance1() throws Exception {

    }

    @Test
    public void testGetPropertys() throws Exception {
//        Property[] propertys = ClassUtil.getPropertys(Article.class);
//        for(Property property : propertys){
//            System.out.println(property.getReadMethod().getAnnotations());
//        }
    }

    @Test
    public void testGetProperty() throws Exception {
        Property property = ClassUtil.getProperty(Article.class, "attributeValues");
        ParameterizedType parameterizedType = property.getGenericType();
        for(Type type : parameterizedType.getActualTypeArguments()){
            System.out.println(type);
        }
    }

    @Test
    public void testForName() throws Exception {
        String className = Array.newInstance(User.class,0).getClass().getName();

        Class<?> clazz = ClassUtil.forName(className);

        LOG.debug(clazz);

        Assert.assertEquals(clazz,Array.newInstance(User.class,0).getClass());

    }

    @Test
    public void testGetValue() throws Exception {

    }

    @Test
    public void testGetDeclaredField() throws Exception {

    }

    @Test
    public void testSetValue() throws Exception {

    }

    @Test
    public void testGetMethodProxy() throws Exception {

    }

    @Test
    public void testIsBasicType() throws Exception {

    }

    @Test
    public void testIsArray() throws Exception {

    }

    @Test
    public void testIsInterface() throws Exception {

    }

    @Test
    public void testIsList() throws Exception {

    }

    @Test
    public void testIsMap() throws Exception {

    }

    @Test
    public void testIsList1() throws Exception {

    }

    @Test
    public void testGetSuperClassGenricType() throws Exception {

    }

    @Test
    public void testIsBeanType() throws Exception {
        LOG.debug(ClassUtil.isBeanType(Sex.female.getClass()));
    }

}