package com.fantasy.attr.typeConverter;


import com.fantasy.security.bean.User;
import com.fantasy.security.service.UserService;
import ognl.DefaultTypeConverter;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.Member;
import java.util.Map;

public class UserIdTypeConverter extends DefaultTypeConverter {

    @Resource
    private UserService userService;

    @Transactional
    public Object convertValue(Map context, Object target, Member member, String propertyName, Object value, Class toType) {
        if(toType == User.class){
            return userService.get(Long.valueOf(value.toString()));
        }
        return super.convertValue(context, target, member, propertyName, value, toType);
    }
}
