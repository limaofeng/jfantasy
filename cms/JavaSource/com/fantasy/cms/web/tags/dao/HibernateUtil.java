package com.fantasy.cms.web.tags.dao;


import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.framework.dao.hibernate.util.ReflectionUtils;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HibernateUtil {
    private static final Log logger = LogFactory.getLog(HibernateUtil.class);
    private static final Map<Class<?>, HibernateDao> cache = new ConcurrentHashMap<Class<?>, HibernateDao>();

    public static <T, PK extends Serializable> HibernateDao<T, PK> getHibernateDao(Class<T> clazz, Class<PK> pk) {
        if (!cache.containsKey(clazz)) {
            HibernateDao hibernateDao = (HibernateDao) SpringContextUtil.autowire(SimpleHibernateDao.class, 2);
            ReflectionUtils.setFieldValue(hibernateDao, "entityClass", clazz);
            cache.put(clazz, hibernateDao);
        }
        return cache.get(clazz);
    }

    public static <T> Pager<T> initialize(Pager<T> pager) {
        initialize(pager.getPageItems(), new Object[0]);
        return pager;
    }

    public static <T> List<T> initialize(List<T> list, Object[] ignores) {
        if (list == null)
            return null;
        ignores = ObjectUtil.join(ignores, new Object[]{list});
        for (Object t : list) {
            initialize(t, ignores);
        }
        return list;
    }

    public static <T> T initialize(T entity, Object[] ignores) {
        if (entity == null)
            return null;
        if (!Hibernate.isInitialized(entity)) {
            Hibernate.initialize(entity);
        }
        Field[] fields = ClassUtil.getDeclaredFields(entity.getClass());
        for (Field field : fields) {
            if (ClassUtil.isPrimitiveOrWrapperOrStringOrDate(field.getType())) {
                continue;
            }
            Object proxy = ClassUtil.getValue(entity, field.getName());
            if (ObjectUtil.indexOf(ignores, proxy) > -1) {
                continue;
            }
            if (ClassUtil.isList(field.getType()))
                initialize((List) proxy, ObjectUtil.join(ignores, new Object[]{entity}));
            else if (ClassUtil.isArray(field.getType()))
                logger.error(field.getName() + "=>" + field.getType() + "=>" + field.getType().getComponentType());
            else {
                initialize(proxy, ObjectUtil.join(ignores, new Object[]{entity}));
            }
        }
        return entity;
    }

    private static class SimpleHibernateDao extends HibernateDao<Object, Long> {
    }
}
