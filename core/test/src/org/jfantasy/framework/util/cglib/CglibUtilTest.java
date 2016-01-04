package org.jfantasy.framework.util.cglib;

import org.jfantasy.framework.util.ognl.OgnlUtil;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import java.lang.reflect.Method;

public class CglibUtilTest {

    private final static Log logger = LogFactory.getLog(CglibUtilTest.class);

    @Test
    public void testNewInstance() throws Exception {
        Object object = CglibUtil.newInstance(CglibUtilBean.class, new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                return methodProxy.invokeSuper(o, objects);
            }
        });
        logger.debug(object);
        OgnlUtil.getInstance().setValue("integer",object,"123456");
        OgnlUtil.getInstance().setValue("string",object,"limaofeng");


    }

    @Test
    public void testGetDefaultInterceptor() throws Exception {
        CglibUtil.getDefaultInterceptor("validator");
    }
}