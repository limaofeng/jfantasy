package org.jfantasy.attr.storage.listener;

import org.jfantasy.attr.framework.CustomBeanFactory;
import org.jfantasy.attr.storage.bean.AttributeVersion;
import org.jfantasy.framework.spring.SpringContextUtil;
import org.jfantasy.framework.util.common.ClassUtil;
import org.hibernate.event.spi.*;
import org.hibernate.persister.entity.EntityPersister;

public class VersionChangedEventListener implements PostInsertEventListener, PostUpdateEventListener, PostDeleteEventListener {

    private CustomBeanFactory customBeanFactory;

    public CustomBeanFactory getCustomBeanFactory() {
        return customBeanFactory != null ? customBeanFactory : (customBeanFactory = SpringContextUtil.getBeanByType(CustomBeanFactory.class));
    }

    @Override
    public void onPostDelete(PostDeleteEvent event) {
        EntityPersister entityPersister = event.getPersister();
        Class<?> clazz = ClassUtil.forName(entityPersister.getRootEntityName());
        assert clazz != null;
        if (AttributeVersion.class.isAssignableFrom(clazz)) {
            getCustomBeanFactory().removeVersion((AttributeVersion) event.getEntity());
        }
    }

    @Override
    public void onPostInsert(PostInsertEvent event) {
        EntityPersister entityPersister = event.getPersister();
        Class<?> clazz = ClassUtil.forName(entityPersister.getRootEntityName());
        assert clazz != null;
        if (AttributeVersion.class.isAssignableFrom(clazz)) {
            getCustomBeanFactory().loadVersion((AttributeVersion) event.getEntity());
        }
    }

    @Override
    public void onPostUpdate(PostUpdateEvent event) {
        EntityPersister entityPersister = event.getPersister();
        Class<?> clazz = ClassUtil.forName(entityPersister.getRootEntityName());
        assert clazz != null;
        if (AttributeVersion.class.isAssignableFrom(clazz)) {
            getCustomBeanFactory().loadVersion((AttributeVersion) event.getEntity());
        }
    }

    @Override
    public boolean requiresPostCommitHanding(EntityPersister persister) {
        return false;
    }

}
