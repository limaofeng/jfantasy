package com.fantasy.framework.util;

import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.security.bean.User;
import org.junit.Test;

import java.util.Arrays;

public class ClassUtilTest {

    @Test
    public void test(){
        System.out.print(Arrays.toString(ObjectUtil.toFieldArray(ClassUtil.getPropertys(User.class),"name",String.class)));
    }

}
