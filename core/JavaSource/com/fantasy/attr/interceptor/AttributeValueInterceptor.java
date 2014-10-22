package com.fantasy.attr.interceptor;

import com.fantasy.attr.DynaBean;
import com.fantasy.attr.bean.AttributeValue;
import com.fantasy.attr.util.VersionUtil;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.dao.hibernate.util.ReflectionUtils;
import com.fantasy.framework.util.common.BeanUtil;
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
    private final static MethodProxy _method_getIdValue = ClassUtil.getMethodProxy(HibernateDao.class, "getIdValue", Class.class, Object.class);

    /**
     * findPager 时，对动态Bean 添加代理
     *
     * @param pjp     ProceedingJoinPoint
     * @param pager   翻页对象
     * @param filters 过滤条件
     * @return pager
     * @throws Throwable
     */
    @Around(value = "execution(public * com.fantasy.framework.dao.hibernate.HibernateDao.findPager(..)) && args(pager,filters)", argNames = "pjp,pager,filters")
    public Object findPager(ProceedingJoinPoint pjp, Pager pager, List<PropertyFilter> filters) throws Throwable {
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
        List<DynaBean> beans = pager.getPageItems();
        for (int i = 0, length = beans.size(); i < length; i++) {
            DynaBean dynaBean = beans.get(i);
            if (dynaBean.getVersion() == null) {
                continue;
            }
            beans.set(i, VersionUtil.makeDynaBean(dynaBean));
        }
        return pager;
    }

    /**
     * 保存代理对象时，将代理对象转为原来的类型
     *
     * @param pjp    ProceedingJoinPoint
     * @param entity hibernateEntity
     * @return object
     * @throws Throwable
     */
    @Around(value = "execution(public * com.fantasy.framework.dao.hibernate.HibernateDao.save(..)) && args(entity)", argNames = "pjp,entity")
    public Object save(ProceedingJoinPoint pjp, Object entity) throws Throwable {
        HibernateDao dao = (HibernateDao) pjp.getTarget();
        Class entityClass = (Class) ClassUtil.getValue(pjp.getTarget(), "entityClass");
        if (entity != null && entity instanceof DynaBean && entity.getClass().getSimpleName().contains("$v") && entityClass.equals(entity.getClass().getSuperclass())) {
            DynaBean dynaBean = (DynaBean) ClassUtil.newInstance(entityClass);
            BeanUtil.copyProperties(dynaBean, entity);
            dynaBean.setAttributeValues(((DynaBean) entity).getAttributeValues());
            for (AttributeValue attributeValue : ((DynaBean) entity).getAttributeValues()) {
                if (attributeValue.getTargetId() == null) {
                    attributeValue.setTargetId((Long) _method_getIdValue.invoke(dao, entityClass, dynaBean));
                }
            }
            entity = dynaBean;
        }
        return pjp.proceed(new Object[]{entity});
    }

    @Around(value = "execution(public * com.fantasy.framework.dao.hibernate.HibernateDao.get(..)) && args(id)", argNames = "pjp,id")
    public Object get(ProceedingJoinPoint pjp, Object id) throws Throwable {
        Class entityClass = (Class) ClassUtil.getValue(pjp.getTarget(), "entityClass");
        Object entity = pjp.proceed();
        if (!DynaBean.class.isAssignableFrom(entityClass)) {
            return entity;
        }
        if (entity != null && entity instanceof DynaBean && !entity.getClass().getSimpleName().contains("$v")) {
            return VersionUtil.makeDynaBean((DynaBean) entity);
        }
        return entity;
    }

}
