package com.fantasy.framework.util.ognl;

import com.fantasy.security.bean.User;
import ognl.Ognl;
import ognl.OgnlContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;

public class OgnlUtilTest {

    private final static Log LOG = LogFactory.getLog(OgnlUtilTest.class);

    @Test
    public void testSetValue(){
        User user = new User();
        OgnlContext ognlContext = (OgnlContext) Ognl.createDefaultContext(user);
        OgnlUtil.getInstance().setValue("id",user,"1");
        OgnlUtil.getInstance().setValue("username", user, "limaofeng");
        OgnlUtil.getInstance().setValue("details.name",user, "limaofeng");
        System.out.println(user);
    }

    @Test
    public void testSetValueByBeanArray(){
        OgnlBean ognlTest = new OgnlBean();

        OgnlUtil.getInstance().setValue("number", ognlTest, "100");
        OgnlUtil.getInstance().setValue("name", ognlTest, "test");

        LOG.debug(ognlTest);

        OgnlUtil.getInstance().setValue("names[0]", ognlTest, "test1");
        OgnlUtil.getInstance().setValue("names[1]", ognlTest, "test2");

        LOG.debug(ognlTest);

        Assert.assertEquals(ognlTest.getNames().length, 2);

        OgnlUtil.getInstance().setValue("listNames[0]", ognlTest, "test1");
        OgnlUtil.getInstance().setValue("listNames[1]", ognlTest, "test2");

        LOG.debug(ognlTest);

        Assert.assertEquals(ognlTest.getListNames().size(), 2);

        OgnlUtil.getInstance().setValue("list[0].number", ognlTest, "100");
        OgnlUtil.getInstance().setValue("list[0].name", ognlTest, "test");

        LOG.debug(ognlTest);

        Assert.assertEquals(ognlTest.getList().size(),1);

        OgnlUtil.getInstance().setValue("bean.array[0].number", ognlTest, "100");
        OgnlUtil.getInstance().setValue("bean.array[0].name", ognlTest, "test");

        OgnlUtil.getInstance().setValue("bean.array[1].number", ognlTest, "100");
        OgnlUtil.getInstance().setValue("bean.array[1].name", ognlTest, "test");

        LOG.debug(ognlTest);

        Assert.assertEquals(ognlTest.getBean().getArray().length, 2);

    }

}
