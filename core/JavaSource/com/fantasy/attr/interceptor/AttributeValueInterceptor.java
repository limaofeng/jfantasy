package com.fantasy.attr.interceptor;

import com.fantasy.attr.DynaBean;
import com.fantasy.attr.bean.Attribute;
import com.fantasy.attr.bean.AttributeValue;
import com.fantasy.attr.util.VersionUtil;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.dao.hibernate.util.ReflectionUtils;
import com.fantasy.framework.util.common.BeanUtil;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.reflect.MethodProxy;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Component
@Aspect
public class AttributeValueInterceptor {

    private final static MethodProxy _method_buildPropertyFilterCriterions = ClassUtil.getMethodProxy(HibernateDao.class, "buildPropertyFilterCriterions");
    private final static MethodProxy _method_buildPropertyFilterCriterion = ClassUtil.getMethodProxy(HibernateDao.class, "buildPropertyFilterCriterion");
    private final static MethodProxy _method_findPager = ClassUtil.getMethodProxy(HibernateDao.class, "findPager", Pager.class, Criterion[].class);
    private final static MethodProxy _method_getIdValue = ClassUtil.getMethodProxy(HibernateDao.class, "getIdValue", Class.class, Object.class);

    private final static MethodProxy _method_get = ClassUtil.getMethodProxy(HibernateDao.class, "get", Serializable.class);

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
            //TODO 暂时不考虑 "LIKES_sn_OR_shipName" 这种方式匹配动态属性的查询
            if (filter.getPropertyNames().length == 1 && ClassUtil.getProperty(entityClass, filter.getPropertyName().split("\\.")[0]) == null) {
                removeFilters.add(filter);
            }
        }
        List<Criterion> attrCriterions = new ArrayList<Criterion>();
        for (PropertyFilter filter : removeFilters) {
            filters.remove(filter);
            attrCriterions.add(Restrictions.and(Restrictions.eq("attributeValues.attribute.code", filter.getPropertyName()), (Criterion) _method_buildPropertyFilterCriterion.invoke(dao, "attributeValues.value", filter.getPropertyValue(String.class), filter.getMatchType())));
        }
        Criterion[] criterions = (Criterion[]) _method_buildPropertyFilterCriterions.invoke(dao, filters);
        criterions = ObjectUtil.join(criterions, attrCriterions.toArray(new Criterion[attrCriterions.size()]));
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

    @Around(value = "execution(public * com.fantasy.framework.dao.hibernate.HibernateDao.findUniqueBy(..))")
    public Object findUniqueBy(ProceedingJoinPoint pjp) throws Throwable {
        Class<?> entityClass = ReflectionUtils.getSuperClassGenricType(pjp.getTarget().getClass());
        if (!DynaBean.class.isAssignableFrom(entityClass)) {
            return pjp.proceed();
        }
        DynaBean dynaBean = (DynaBean) pjp.proceed();
        if (dynaBean == null || dynaBean.getVersion() == null) {
            return dynaBean;
        }
        return VersionUtil.makeDynaBean(dynaBean);
    }

    @Around(value = "execution(public * com.fantasy.framework.dao.hibernate.HibernateDao.findUnique(..))")
    public Object findUnique(ProceedingJoinPoint pjp) throws Throwable {
        Class<?> entityClass = ReflectionUtils.getSuperClassGenricType(pjp.getTarget().getClass());
        if (!DynaBean.class.isAssignableFrom(entityClass)) {
            return pjp.proceed();
        }
        DynaBean dynaBean = (DynaBean) pjp.proceed();
        if (dynaBean == null || dynaBean.getVersion() == null) {
            return dynaBean;
        }
        return VersionUtil.makeDynaBean(dynaBean);
    }

    /**
     * find 时，对动态Bean 添加代理
     *
     * @param pjp ProceedingJoinPoint
     * @return Object
     * @throws Throwable
     */
    @Around(value = "execution(public * com.fantasy.framework.dao.hibernate.HibernateDao.find(..))")
    public Object find(ProceedingJoinPoint pjp) throws Throwable {
        Class<?> entityClass = ReflectionUtils.getSuperClassGenricType(pjp.getTarget().getClass());
        if (!DynaBean.class.isAssignableFrom(entityClass)) {
            return pjp.proceed();
        }
        List<DynaBean> beans = (List) pjp.proceed();
        for (int i = 0, length = beans.size(); i < length; i++) {
            DynaBean dynaBean = beans.get(i);
            if (dynaBean.getVersion() == null) {
                continue;
            }
            beans.set(i, VersionUtil.makeDynaBean(dynaBean));
        }
        return beans;
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
        if (!(entity != null && entity instanceof DynaBean && entity.getClass().getName().contains("$v") && entityClass.equals(entity.getClass().getSuperclass()))) {
            return pjp.proceed(new Object[]{entity});
        }
        Long entityId = (Long) _method_getIdValue.invoke(dao, entityClass, entity);
        DynaBean dynaBean = (DynaBean) (entityId == null ? ClassUtil.newInstance(entityClass) : _method_get.invoke(dao, entityId));
        BeanUtil.copyProperties(dynaBean, entity, "attributeValues");
        List<AttributeValue> attributeValues = dynaBean.getAttributeValues() == null ? new ArrayList<AttributeValue>() : dynaBean.getAttributeValues();
        for (Attribute attribute : ((DynaBean) entity).getVersion().getAttributes()) {
            AttributeValue attributeValue = ObjectUtil.find(attributeValues, "attribute.code", attribute.getCode());
            if (attributeValue == null) {
                attributeValue = new AttributeValue();
                attributeValue.setTargetId(entityId);
                attributeValue.setAttribute(attribute);
                attributeValue.setVersion(((DynaBean) entity).getVersion());
            }
            String value = VersionUtil.getOgnlUtil(attribute.getAttributeType()).getValue(attribute.getCode(), entity, String.class);
            if (StringUtil.isNotBlank(value)) {
                attributeValue.setValue(value);
                attributeValues.add(attributeValue);
            } else {
                ObjectUtil.remove(attributeValues, "attribute.code", attribute.getCode());
            }
        }
        Object oldEntity = entity;
        entity = dynaBean;
        Object retval = pjp.proceed(new Object[]{entity});
        Long newEntityId = (Long) _method_getIdValue.invoke(dao, entityClass, entity);
        if (entityId != null || newEntityId == null) {
            BeanUtil.copyProperties(oldEntity, entity);
            return retval;
        }
        for (AttributeValue attributeValue : attributeValues) {
            attributeValue.setTargetId(newEntityId);
        }
        ((DynaBean) entity).setAttributeValues(attributeValues);
        retval = pjp.proceed(new Object[]{entity});
        BeanUtil.copyProperties(oldEntity, entity);
        return retval;
    }

    @Around(value = "execution(public * com.fantasy.framework.dao.hibernate.HibernateDao.get(..)) && args(id)", argNames = "pjp,id")
    public Object get(ProceedingJoinPoint pjp, Object id) throws Throwable {
        Class<?> entityClass = ReflectionUtils.getSuperClassGenricType(pjp.getTarget().getClass());
        if (!DynaBean.class.isAssignableFrom(entityClass)) {
            return pjp.proceed();
        }
        DynaBean dynaBean = (DynaBean) pjp.proceed();
        if (dynaBean == null || dynaBean.getVersion() == null) {
            return dynaBean;
        }
        return VersionUtil.makeDynaBean(dynaBean);
    }

}
