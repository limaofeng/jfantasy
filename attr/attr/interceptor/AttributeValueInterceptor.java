package org.jfantasy.attr.interceptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.hibernate.Hibernate;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.collection.spi.PersistentCollection;
import org.jfantasy.attr.framework.CustomBeanFactory;
import org.jfantasy.attr.framework.DynaBean;
import org.jfantasy.attr.framework.query.DynaBeanQuery;
import org.jfantasy.attr.framework.query.DynaBeanQueryManager;
import org.jfantasy.attr.storage.bean.Attribute;
import org.jfantasy.attr.storage.bean.AttributeValue;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.dao.hibernate.util.ReflectionUtils;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.util.reflect.MethodProxy;
import org.jfantasy.framework.util.reflect.Property;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
@Component
@Aspect*/
public class AttributeValueInterceptor {

    private final static Log LOG = LogFactory.getLog(AttributeValueInterceptor.class);

    private final static MethodProxy _method_getIdValue = ClassUtil.getMethodProxy(HibernateDao.class, "getIdValue", Class.class, Object.class);

    private final static MethodProxy _method_get = ClassUtil.getMethodProxy(HibernateDao.class, "get", Serializable.class);

    @Autowired
    private CustomBeanFactory customBeanFactory;

    /**
     * findPager 时，对动态Bean 添加代理
     *
     * @param pjp     ProceedingJoinPoint
     * @param pager   翻页对象
     * @param filters 过滤条件
     * @return pager
     * @throws Throwable
     */
    @Around(value = "execution(public * org.jfantasy.framework.dao.hibernate.HibernateDao.findPager(..)) && args(pager,filters)", argNames = "pjp,pager,filters")
    public Object findPager(ProceedingJoinPoint pjp, Pager pager, List<PropertyFilter> filters) throws Throwable {
        Class<?> entityClass = ClassUtil.getValue(pjp.getTarget(), "entityClass");
        if (!DynaBean.class.isAssignableFrom(entityClass)) {
            return pjp.proceed();
        }
        try {
            DynaBeanQueryManager.getManager().push(DynaBeanQuery.createDynaBeanQuery());
            return toDynaBean(pjp.proceed(prepare(pjp)));
        } finally {
            DynaBeanQueryManager.getManager().pop();
        }
    }

    @Around(value = "execution(public * org.jfantasy.framework.dao.hibernate.HibernateDao.findUniqueBy(..))")
    public Object findUniqueBy(ProceedingJoinPoint pjp) throws Throwable {
        Class<?> entityClass = ReflectionUtils.getSuperClassGenricType(pjp.getTarget().getClass());
        if (!DynaBean.class.isAssignableFrom(entityClass)) {
            return pjp.proceed();
        }
        try {
            DynaBeanQueryManager.getManager().push(DynaBeanQuery.createDynaBeanQuery());
            return toDynaBean(pjp.proceed(prepare(pjp)));
        } finally {
            DynaBeanQueryManager.getManager().pop();
        }
    }

    @Around(value = "execution(public * org.jfantasy.framework.dao.hibernate.HibernateDao.findUnique(..))")
    public Object findUnique(ProceedingJoinPoint pjp) throws Throwable {
        Class<?> entityClass = ClassUtil.getValue(pjp.getTarget(), "entityClass");
        if (!DynaBean.class.isAssignableFrom(entityClass)) {
            return pjp.proceed();
        }
        try {
            DynaBeanQueryManager.getManager().push(DynaBeanQuery.createDynaBeanQuery());
            return toDynaBean(pjp.proceed(prepare(pjp)));
        } finally {
            DynaBeanQueryManager.getManager().pop();
        }
    }

    /**
     * find 时，对动态Bean 添加代理
     *
     * @param pjp ProceedingJoinPoint
     * @return Object
     * @throws Throwable
     */
    @Around(value = "execution(public * org.jfantasy.framework.dao.hibernate.HibernateDao.find(..))")
    public Object find(ProceedingJoinPoint pjp) throws Throwable {
        Class<?> entityClass = (HibernateDao)pjp.getTarget();
        if (!DynaBean.class.isAssignableFrom(entityClass)) {
            return pjp.proceed();
        }
        try {
            DynaBeanQueryManager.getManager().push(DynaBeanQuery.createDynaBeanQuery());
            Object retval = pjp.proceed(prepare(pjp));
            try {
                return toDynaBean(retval);
            }catch (Exception ex){
                LOG.error(ex.getMessage(),ex);
                return retval;
            }
        } finally {
            DynaBeanQueryManager.getManager().pop();
        }
    }

    private Object toDynaBean(Object bean) {
        if (bean == null) {
            return null;
        }
        if (bean instanceof Pager) {
            Pager pager = (Pager) bean;
            toDynaBean(pager.getPageItems());
            return pager;
        } else if (bean instanceof List) {
            List beans = (List) bean;
            for (int i = 0, length = beans.size(); i < length; i++) {
                DynaBean dynaBean = (DynaBean) beans.get(i);
                if (dynaBean.getVersion() == null) {
                    continue;
                }
                beans.set(i, toDynaBean(dynaBean));
            }
            return beans;
        } else {
            return toDynaBean((DynaBean) bean);
        }
    }

    private Object toDynaBean(DynaBean dynaBean) {
        if (dynaBean == null) {
            return null;
        }
        try {
            return customBeanFactory.makeDynaBean(dynaBean);
        } catch (ObjectNotFoundException e) {
            LOG.error(e.getMessage(), e);
            return dynaBean;
        }
    }

    private Object[] prepare(ProceedingJoinPoint pjp) {
        Class<?> entityClass = ClassUtil.getValue(pjp.getTarget(), "entityClass");
        DynaBeanQuery dynaBeanQuery = DynaBeanQueryManager.getManager().peek();
        Property[] properties = ClassUtil.getPropertys(entityClass);
        Object[] args = pjp.getArgs();
        for (Object arg : args) {
            if (arg instanceof List) {
                for (PropertyFilter filter : (List<PropertyFilter>) arg) {
                    for (String propertyName : filter.getPropertyNames()) {
                        String simpleName = propertyName.contains(".") ? propertyName.substring(0, propertyName.indexOf(".")) : propertyName;
                        if (ObjectUtil.find(properties, "name", simpleName) != null) {
                            continue;
                        }
                        if (simpleName.equals(propertyName)) {
                            dynaBeanQuery.addColumn(propertyName, filter.getPropertyType());
                        } else {
                            Attribute attribute = customBeanFactory.getAttribute(entityClass, propertyName);
                            if (attribute != null && StringUtil.isNotBlank(attribute.getAttributeType().getForeignKey())) {
                                dynaBeanQuery.addColumn(attribute.getCode(), ClassUtil.forName(attribute.getAttributeType().getDataType()), attribute.getAttributeType().getForeignKey());
                            }
                        }
                    }
                }
            } else if (arg instanceof Criterion[]) {
                for (Criterion c : (Criterion[]) arg) {
                    if (c instanceof Disjunction) {
                        List<Criterion> criterions = ReflectionUtils.getFieldValue(c, "conditions");
                        for (Criterion criterion : criterions) {
                            LOG.error("未处理：" + criterion.toString());
                        }
                    } else if (c instanceof SQLCriterion) {
                        LOG.error("未处理：" + c.toString());
                    } else if (c instanceof LogicalExpression) {
                        LOG.error("未处理：" + c.toString());
                    } else if (c instanceof NotExpression) {
                        LOG.error("未处理：" + c.toString());
                    } else {
                        String propertyName = ReflectionUtils.getFieldValue(c, "propertyName");
                        String simpleName = propertyName.contains(".") ? propertyName.substring(0, propertyName.indexOf(".")) : propertyName;
                        if (ObjectUtil.find(properties, "name", simpleName) != null) {
                            continue;
                        }
                        if (simpleName.equals(propertyName)) {
                            Object value = ReflectionUtils.getFieldValue(c, "value");
                            dynaBeanQuery.addColumn(propertyName, value.getClass());
                        } else {
                            Attribute attribute = customBeanFactory.getAttribute(entityClass, propertyName);
                            if (attribute != null && StringUtil.isNotBlank(attribute.getAttributeType().getForeignKey())) {
                                dynaBeanQuery.addColumn(attribute.getCode(), ClassUtil.forName(attribute.getAttributeType().getDataType()), attribute.getAttributeType().getForeignKey());
                            }
                        }
                    }
                }
            }
        }
        return args;
    }

    /**
     * 保存代理对象时，将代理对象转为原来的类型
     *
     * @param pjp    ProceedingJoinPoint
     * @param entity hibernateEntity
     * @return object
     * @throws Throwable
     */
    @Around(value = "execution(public * org.jfantasy.framework.dao.hibernate.HibernateDao.save(..)) && args(entity)", argNames = "pjp,entity")
    public Object save(ProceedingJoinPoint pjp, Object entity) throws Throwable {
        HibernateDao dao = (HibernateDao) pjp.getTarget();
        Class entityClass = ClassUtil.getValue(pjp.getTarget(), "entityClass");
        if (!(entity != null && entity instanceof DynaBean && entity.getClass().getName().contains(ClassUtil.CGLIB_CLASS_SEPARATOR) && entityClass == entity.getClass().getSuperclass())) {
            return pjp.proceed(new Object[]{entity});
        }
        Long entityId = (Long) _method_getIdValue.invoke(dao, entityClass, entity);
        DynaBean dynaBean = (DynaBean) (entityId == null ? ClassUtil.newInstance(entityClass) : _method_get.invoke(dao, entityId));
        if (dynaBean == null) {
            dynaBean = (DynaBean) ClassUtil.newInstance(entityClass);
        }
        BeanUtil.copyProperties(dynaBean, entity, "attributeValues");
        assert dynaBean != null;
        Hibernate.initialize(dynaBean.getAttributeValues());
        List<AttributeValue> attributeValues = dynaBean.getAttributeValues();
        if (attributeValues == null || (attributeValues instanceof PersistentCollection && ((PersistentCollection) attributeValues).isWrapper(null)) || attributeValues.isEmpty()) {
            attributeValues = new ArrayList<AttributeValue>();
        }
        for (Attribute attribute : ((DynaBean) entity).getVersion().getAttributes()) {
            AttributeValue attributeValue = ObjectUtil.find(attributeValues, "attribute.code", attribute.getCode());
            if (attributeValue == null) {
                attributeValue = new AttributeValue();
                attributeValue.setTargetId(entityId);
                attributeValue.setAttribute(attribute);
                attributeValue.setVersion(((DynaBean) entity).getVersion());
                attributeValues.add(attributeValue);
            }
            String value = customBeanFactory.getOgnlUtil(attribute.getAttributeType()).getValue(attribute.getCode(), entity, String.class);
            if (StringUtil.isNotBlank(value)) {
                attributeValue.setValue(value);
            } else {
                ObjectUtil.remove(attributeValues, "attribute.code", attribute.getCode());
            }
        }
        Object oldEntity = entity;
        entity = dynaBean;
        ((DynaBean) entity).setAttributeValues(attributeValues);
        ((DynaBean) entity).setAttributeValueStore(JSON.serialize(attributeValues));
        Object retval = pjp.proceed(new Object[]{entity});
        Long newEntityId = (Long) _method_getIdValue.invoke(dao, entityClass, entity);
        if (entityId != null || newEntityId == null) {
            BeanUtil.copyProperties(oldEntity, entity);
            return retval;
        }
        for (AttributeValue attributeValue : attributeValues) {
            attributeValue.setTargetId(newEntityId);
        }
        ((DynaBean) entity).setAttributeValueStore(JSON.serialize(attributeValues));
        BeanUtil.copyProperties(oldEntity, entity);
        return entity;
    }

    @Around(value = "execution(public * org.jfantasy.framework.dao.hibernate.HibernateDao.get(..)) && args(id)", argNames = "pjp,id")
    public Object get(ProceedingJoinPoint pjp, Serializable id) throws Throwable {
        Class<?> entityClass = ReflectionUtils.getSuperClassGenricType(pjp.getTarget().getClass());
        if (!DynaBean.class.isAssignableFrom(entityClass)) {
            return pjp.proceed();
        }
        return toDynaBean(pjp.proceed());
    }

    @Around(value = "execution(public * org.jfantasy.framework.lucene.dao.hibernate.HibernateLuceneDao.get(..)) && args(id)", argNames = "pjp,id")
    public Object get(final ProceedingJoinPoint pjp, String id) throws Throwable {
        Class<?> entityClass = ReflectionUtils.getSuperClassGenricType(pjp.getTarget().getClass());
        if (!DynaBean.class.isAssignableFrom(entityClass)) {
            return pjp.proceed();
        }
        return JdbcUtil.transaction(new JdbcUtil.Callback<Object>() {
            @Override
            public Object run() {
                try {
                    return toDynaBean(pjp.proceed());
                } catch (Throwable throwable) {//NOSONAR
                    LOG.error(throwable.getMessage());
                    return null;
                }
            }
        });
    }

    /**
     * find 时，对动态Bean 添加代理
     *
     * @param pjp ProceedingJoinPoint
     * @return Object
     * @throws Throwable
     */
    @Around(value = "execution(public * org.jfantasy.framework.lucene.dao.hibernate.HibernateLuceneDao.find(..))")
    public Object findByLuceneDao(ProceedingJoinPoint pjp) throws Throwable {
        Class<?> entityClass = ClassUtil.getValue(pjp.getTarget(), "entityClass");
        if (!DynaBean.class.isAssignableFrom(entityClass)) {
            return pjp.proceed();
        }
        try {
            DynaBeanQueryManager.getManager().push(DynaBeanQuery.createDynaBeanQuery());
            return toDynaBean(pjp.proceed(prepare(pjp)));
        } finally {
            DynaBeanQueryManager.getManager().pop();
        }
    }

    @Around(value = "execution(public * org.jfantasy.framework.lucene.dao.hibernate.HibernateLuceneDao.findByField(..))")
    public Object findByField(ProceedingJoinPoint pjp) throws Throwable {
        Class<?> entityClass = ClassUtil.getValue(pjp.getTarget(), "entityClass");
        if (!DynaBean.class.isAssignableFrom(entityClass)) {
            return pjp.proceed();
        }
        try {
            DynaBeanQueryManager.getManager().push(DynaBeanQuery.createDynaBeanQuery());
            return toDynaBean(pjp.proceed(prepare(pjp)));
        } finally {
            DynaBeanQueryManager.getManager().pop();
        }
    }

}
