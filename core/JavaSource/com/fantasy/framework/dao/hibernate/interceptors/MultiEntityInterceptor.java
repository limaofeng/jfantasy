package com.fantasy.framework.dao.hibernate.interceptors;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.hibernate.CallbackException;
import org.hibernate.EntityMode;
import org.hibernate.Interceptor;
import org.hibernate.Transaction;
import org.hibernate.type.Type;

/**
 * hibernate Interceptor 多实现
 *
 * @author 李茂峰
 * @version 1.0
 * @功能描述 默认的hibernate拦截器只能配置一个
 * @since 2013-9-12 下午5:01:53
 */
@SuppressWarnings("rawtypes")
public class MultiEntityInterceptor implements Interceptor {

    private Set<Interceptor> interceptors = new HashSet<Interceptor>();

    public void addInterceptors(Interceptor interceptor) {
        this.interceptors.add(interceptor);
    }

    public void afterTransactionBegin(Transaction transaction) {
        for (Interceptor interceptor : this.interceptors) {
            interceptor.afterTransactionBegin(transaction);
        }
    }

    public void afterTransactionCompletion(Transaction transaction) {
        for (Interceptor interceptor : this.interceptors) {
            interceptor.afterTransactionCompletion(transaction);
        }
    }

    public void beforeTransactionCompletion(Transaction transaction) {
        for (Interceptor interceptor : this.interceptors) {
            interceptor.beforeTransactionCompletion(transaction);
        }
    }

    public int[] findDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
        int[] retVal = new int[0];
        for (Interceptor interceptor : this.interceptors) {
            retVal = interceptor.findDirty(entity, id, currentState, previousState, propertyNames, types);
        }
        return retVal;
    }

    public Object getEntity(String entityClass, Serializable id) throws CallbackException {
        Object retVal = null;
        for (Interceptor interceptor : this.interceptors) {
            retVal = interceptor.getEntity(entityClass, id);
        }
        return retVal;
    }

    public String getEntityName(Object entity) throws CallbackException {
        String retVal = null;
        for (Interceptor interceptor : this.interceptors) {
            retVal = interceptor.getEntityName(entity);
        }
        return retVal;
    }

    public Object instantiate(String entityName, EntityMode entityMode, Serializable id) throws CallbackException {
        Object retVal = null;
        for (Interceptor interceptor : this.interceptors) {
            retVal = interceptor.instantiate(entityName, entityMode, id);
        }
        return retVal;
    }

    public Boolean isTransient(Object entity) {
        Boolean retVal = null;
        for (Interceptor interceptor : this.interceptors) {
            retVal = interceptor.isTransient(entity);
        }
        return retVal;
    }

    public void onCollectionRecreate(Object entity, Serializable id) throws CallbackException {
        for (Interceptor interceptor : this.interceptors) {
            interceptor.onCollectionRecreate(entity, id);
        }
    }

    public void onCollectionRemove(Object entity, Serializable id) throws CallbackException {
        for (Interceptor interceptor : this.interceptors) {
            interceptor.onCollectionRemove(entity, id);
        }
    }

    public void onCollectionUpdate(Object entity, Serializable id) throws CallbackException {
        for (Interceptor interceptor : this.interceptors) {
            interceptor.onCollectionUpdate(entity, id);
        }
    }

    public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) throws CallbackException {
        for (Interceptor interceptor : this.interceptors) {
            interceptor.onDelete(entity, id, state, propertyNames, types);
        }
    }

    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) throws CallbackException {
        boolean retVal = true;
        for (Interceptor interceptor : this.interceptors) {
            retVal = interceptor.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
        }
        return retVal;
    }

    public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) throws CallbackException {
        boolean retVal = true;
        for (Interceptor interceptor : this.interceptors) {
            retVal = interceptor.onLoad(entity, id, state, propertyNames, types);
        }
        return retVal;
    }

    public String onPrepareStatement(String entityName) {
        String retVal = "";
        for (Interceptor interceptor : this.interceptors) {
            retVal = interceptor.onPrepareStatement(entityName);
        }
        return retVal;
    }

    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) throws CallbackException {
        boolean retVal = true;
        for (Interceptor interceptor : this.interceptors) {
            retVal = interceptor.onSave(entity, id, state, propertyNames, types);
        }
        return retVal;
    }

    public void postFlush(Iterator iterator) throws CallbackException {
        for (Interceptor interceptor : this.interceptors) {
            interceptor.postFlush(iterator);
        }
    }

    public void preFlush(Iterator iterator) throws CallbackException {
        for (Interceptor interceptor : this.interceptors) {
            interceptor.preFlush(iterator);
        }
    }

}
