package com.fantasy.attr.interceptor;

import com.fantasy.attr.DynaBean;
import com.fantasy.attr.util.VersionUtil;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.dao.hibernate.util.ReflectionUtils;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.reflect.MethodProxy;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Aspect
public class AttributeValueInterceptor {

    private final static MethodProxy _method_buildPropertyFilterCriterions = ClassUtil.getMethodProxy(HibernateDao.class, "buildPropertyFilterCriterions");
    private final static MethodProxy _method_buildPropertyFilterCriterion = ClassUtil.getMethodProxy(HibernateDao.class, "buildPropertyFilterCriterion");
    private final static MethodProxy _method_findPager = ClassUtil.getMethodProxy(HibernateDao.class, "findPager", Pager.class, Criterion[].class);

    @Around(value = "execution(public * com.fantasy.framework.dao.hibernate.HibernateDao.findPager(..)) && args(pager,filters)", argNames = "pjp,pager,filters")
    public Object proceed(ProceedingJoinPoint pjp, Pager pager, List<PropertyFilter> filters) throws Throwable {
        Class<?> entityClass = ReflectionUtils.getSuperClassGenricType(pjp.getTarget().getClass());
        HibernateDao dao = (HibernateDao) pjp.getTarget();
        if (!DynaBean.class.isAssignableFrom(entityClass)) {
            return pjp.proceed();
        }
        List<PropertyFilter> removeFilters = new ArrayList<PropertyFilter>();
        for (PropertyFilter filter : filters) {
            if (ClassUtil.getProperty(entityClass, filter.getPropertyName()) == null) {
                removeFilters.add(filter);
            }
        }
        List<Criterion> attrCriterions = new ArrayList<Criterion>();
        for (PropertyFilter filter : removeFilters) {
            filters.remove(filter);
            attrCriterions.add(Restrictions.and(Restrictions.eq("attributeValues.attribute.code", filter.getPropertyName()), (Criterion) _method_buildPropertyFilterCriterion.invoke(dao, "attributeValues.value", filter.getPropertyValue(String.class), filter.getMatchType())));
        }
        Criterion[] criterions = (Criterion[]) _method_buildPropertyFilterCriterions.invoke(dao, filters);
        for (Criterion criterion : attrCriterions) {
            criterions = ObjectUtil.join(criterions, criterion);
        }
        pager = (Pager) _method_findPager.invoke(dao, pager, criterions);

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

}
