package com.fantasy.attr.storage.listener;

import com.fantasy.attr.framework.CustomBeanFactory;
import com.fantasy.attr.storage.bean.AttributeVersion;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.common.ClassUtil;
import org.hibernate.event.spi.*;
import org.hibernate.persister.entity.EntityPersister;

public class VersionChangedEventListener implements PostInsertEventListener, PostUpdateEventListener, PostDeleteEventListener {

//    private static final Log LOG = LogFactory.getLog(VersionChangedEventListener.class);

    private CustomBeanFactory customBeanFactory;

    public CustomBeanFactory getCustomBeanFactory() {
        return customBeanFactory != null ? customBeanFactory : (customBeanFactory = SpringContextUtil.getBeanByType(CustomBeanFactory.class));
    }

    @Override
    public void onPostDelete(PostDeleteEvent event) {
        EntityPersister entityPersister = event.getPersister();
        Class<?> clazz = ClassUtil.forName(entityPersister.getRootEntityName());
        if (AttributeVersion.class.isAssignableFrom(clazz)) {
            getCustomBeanFactory().removeVersion((AttributeVersion) event.getEntity());
        }
    }

    @Override
    public void onPostInsert(PostInsertEvent event) {
        EntityPersister entityPersister = event.getPersister();
        Class<?> clazz = ClassUtil.forName(entityPersister.getRootEntityName());
        if (AttributeVersion.class.isAssignableFrom(clazz)) {
            getCustomBeanFactory().loadVersion((AttributeVersion) event.getEntity());
        }
    }

    @Override
    public void onPostUpdate(PostUpdateEvent event) {
        EntityPersister entityPersister = event.getPersister();
        Class<?> clazz = ClassUtil.forName(entityPersister.getRootEntityName());
        if (AttributeVersion.class.isAssignableFrom(clazz)) {
            getCustomBeanFactory().loadVersion((AttributeVersion) event.getEntity());
        }
    }

    @Override
    public boolean requiresPostCommitHanding(EntityPersister persister) {
        return false;
    }

}
