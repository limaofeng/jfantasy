package com.fantasy.attr.interceptor;

import com.fantasy.attr.DynaBean;
import com.fantasy.attr.util.VersionUtil;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.dao.hibernate.util.ReflectionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Aspect
public class AttributeValueInterceptor implements InitializingBean {

    @Around(value = "execution(public * com.fantasy.framework.dao.hibernate.HibernateDao.findPager(..)) && args(pager,filters)", argNames = "pjp,pager,filters")
    public Object proceed(ProceedingJoinPoint pjp, Pager pager, List<PropertyFilter> filters) throws Throwable {
        if (!DynaBean.class.isAssignableFrom(ReflectionUtils.getSuperClassGenricType(pjp.getTarget().getClass()))) {
            return pjp.proceed();
        }
        System.out.println("==>" + pjp.getStaticPart());
        for (Object object : pjp.getArgs()) {
            System.out.println(object.getClass());
        }
        System.out.println("已经记录下操作日志@Around 方法执行前");
        pager = (Pager) pjp.proceed();
        List beans = pager.getPageItems();
        for (int i = 0, length = beans.size(); i < length; i++) {
            DynaBean dynaBean = (DynaBean) beans.get(i);
            if (dynaBean.getVersion() == null) {
                continue;
            }
            beans.set(i, VersionUtil.makeDynaBean(dynaBean));
        }
        return pager;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("===afterPropertiesSet===>");
    }

}
