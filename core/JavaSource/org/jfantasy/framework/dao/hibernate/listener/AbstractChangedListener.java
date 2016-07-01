package org.jfantasy.framework.dao.hibernate.listener;

import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.jfantasy.framework.dao.hibernate.util.ReflectionUtils;
import org.jfantasy.framework.util.common.ObjectUtil;

import java.util.Arrays;

public abstract class AbstractChangedListener<T> implements PostUpdateEventListener{

    private Class<T> entityClass;

    protected AbstractChangedListener(){
        this.entityClass = ReflectionUtils.getSuperClassGenricType(getClass());
    }

    protected boolean missing(PostInsertEvent event){
        return !event.getEntity().getClass().isAssignableFrom(entityClass);
    }

    protected boolean missing(PostUpdateEvent event){
        return !event.getEntity().getClass().isAssignableFrom(entityClass);
    }

    public T getEntity(PostInsertEvent event){
        return entityClass.cast(event.getEntity());
    }

    public T getEntity(PostUpdateEvent event){
        return entityClass.cast(event.getEntity());
    }

    public boolean modify(PostUpdateEvent event, String property) {
        Arrays.binarySearch(event.getPersister().getPropertyNames(), property);
        int index = ObjectUtil.indexOf(event.getPersister().getPropertyNames(), property);
        return index != -1 && event.getState()[index].equals(event.getOldState()[index]);
    }

}
