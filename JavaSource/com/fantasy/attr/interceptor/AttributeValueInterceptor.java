package com.fantasy.attr.interceptor;

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
public class AttributeValueInterceptor implements InitializingBean{

	@Around(value = "execution(public * com.fantasy.framework.dao.hibernate.HibernateDao.findPager(..)) && args(pager,filters)", argNames = "pjp,pager,filters")
	public Object proceed(ProceedingJoinPoint pjp,Pager pager,List<PropertyFilter> filters) throws Throwable {
        System.out.println(ReflectionUtils.getSuperClassGenricType(pjp.getTarget().getClass()));
        System.out.println("==>"+pjp.getStaticPart());
		for(Object object : pjp.getArgs()){
			System.out.println(object.getClass());
		}
        System.out.println("已经记录下操作日志@Around 方法执行前");
        Object rev = pjp.proceed();
        System.out.println("已经记录下操作日志@Around 方法执行后");
        return rev;
	}

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("===afterPropertiesSet===>");
    }

}
