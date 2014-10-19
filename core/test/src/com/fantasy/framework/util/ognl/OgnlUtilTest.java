package com.fantasy.framework.util.ognl;

import com.fantasy.security.bean.User;
import ognl.Ognl;
import ognl.OgnlContext;
import org.junit.Test;

public class OgnlUtilTest {

    @Test
    public void testSetValue(){
        User user = new User();
        OgnlContext ognlContext = (OgnlContext) Ognl.createDefaultContext(user);
        OgnlUtil.getInstance().setValue("id",user,"1");
        OgnlUtil.getInstance().setValue("username", user, "limaofeng");
        OgnlUtil.getInstance().setValue("details.name",user, "limaofeng");
        System.out.println(user);
    }

}
