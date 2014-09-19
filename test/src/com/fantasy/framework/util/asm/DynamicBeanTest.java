package com.fantasy.framework.util.asm;

import com.fantasy.framework.util.ognl.OgnlUtil;
import org.apache.commons.beanutils.*;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

public class DynamicBeanTest {

    @Test
    public void test() throws IllegalAccessException, InstantiationException,InvocationTargetException, NoSuchMethodException {
        DynaProperty[] dynaProperties = new DynaProperty[]{new DynaProperty("name", String.class),new DynaProperty("age", Integer.class)};
        BasicDynaClass basicDynaClass = new BasicDynaClass("person",BasicDynaBean.class, dynaProperties);
        DynaBean personBean = basicDynaClass.newInstance();
        personBean.set("name", "zhangming");
        personBean.set("age", 123);
        System.out.println("-----------");
        System.out.println(personBean.get("name"));
        System.out.println(personBean.get("age"));
        System.out.println("......");

        System.out.println(OgnlUtil.getInstance().getValue("name",personBean));

        System.out.println(PropertyUtils.getSimpleProperty(personBean, "name"));
    }

}
