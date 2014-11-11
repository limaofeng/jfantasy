package com.fantasy.attr.typeConverter;

import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.ognl.OgnlUtil;
import com.fantasy.security.bean.User;
import junit.framework.Assert;
import ognl.TypeConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext-schedule.xml"})
public class UserIdTypeConverterTest {

    private final static Log logger = LogFactory.getLog(UserIdTypeConverterTest.class);

    @Test
    public void testConvertValue() throws Exception {
        TypeConverter typeConverter = SpringContextUtil.createBean(UserTypeConverter.class,SpringContextUtil.AUTOWIRE_BY_TYPE);

        Object user = typeConverter.convertValue(new HashMap(),null,null,null,"admin",User.class);

        logger.debug(user);

        Assert.assertEquals(user.getClass(), User.class);

        Object username = typeConverter.convertValue(new HashMap(),null,null,null,user,String.class);

        logger.debug(username);

        Assert.assertEquals(username, "admin");

        OgnlUtil ognlUtil = OgnlUtil.getInstance("attr");
        ognlUtil.addTypeConverter(User.class, typeConverter);

        TypeBean typeBean = new TypeBean();
        ognlUtil.setValue("key",typeBean,"test");
        ognlUtil.setValue("user",typeBean,"admin");

        logger.debug(typeBean);

        Assert.assertNotNull(typeBean.getUser());
        Assert.assertEquals(typeBean.getUser().getUsername(),"admin");

        user = ognlUtil.getValue("user",typeBean);

        logger.debug(user);

        username = ognlUtil.getValue("user",typeBean,String.class);

        logger.debug(username);
        Assert.assertEquals(username,"admin");

        /*
		Object obj = ConvertUtils.convert("true", boolean.class);

		System.out.println(obj + "-" + obj.getClass());

		obj = ConvertUtils.convert("12", long.class);

		System.out.println(obj + "-" + obj.getClass());
        Converter converter = new Converter();
        converter.setTypeConverter("double");
        covertIn("15", converter);*/
    }
}