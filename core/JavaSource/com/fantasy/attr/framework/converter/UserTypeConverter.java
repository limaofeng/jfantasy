package com.fantasy.attr.framework.converter;


import com.fantasy.framework.util.ognl.OgnlUtil;
import com.fantasy.security.bean.User;
import com.fantasy.security.service.UserService;
import ognl.DefaultTypeConverter;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import java.lang.reflect.Member;
import java.util.Map;

public class UserTypeConverter extends DefaultTypeConverter {

    @Autowired
    private UserService userService;

    @Transactional
    public Object convertValue(Map context, Object target, Member member, String propertyName, Object value, Class toType) {
        if (toType == User.class) {
            return userService.findUniqueByUsername((String) value);
        } else if (value instanceof User && toType == String.class) {
            return OgnlUtil.getInstance().getValue("username", value);
        }
        return super.convertValue(context, target, member, propertyName, value, toType);
    }

}
