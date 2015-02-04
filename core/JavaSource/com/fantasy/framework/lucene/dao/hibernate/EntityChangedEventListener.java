package com.fantasy.framework.lucene.dao.hibernate;

import com.fantasy.framework.lucene.backend.EntityChangedListener;
import com.fantasy.framework.lucene.backend.IndexChecker;
import com.fantasy.framework.lucene.cache.DaoCache;
import com.fantasy.framework.util.common.ClassUtil;
import org.hibernate.event.spi.*;
import org.hibernate.persister.entity.EntityPersister;

public class EntityChangedEventListener implements PostInsertEventListener, PostUpdateEventListener, PostDeleteEventListener {

    private static final long serialVersionUID = -4339024045294333782L;

    public void onPostInsert(PostInsertEvent event) {
        Object entity = event.getEntity();
        EntityPersister entityPersister = event.getPersister();
        Class<?> clazz = ClassUtil.forName(entityPersister.getRootEntityName());
        if (!IndexChecker.hasIndexed(clazz)) {
            return;
        }
        EntityChangedListener luceneListener = DaoCache.getInstance().get(clazz).getLuceneListener();
        if (luceneListener != null) {
            luceneListener.entityInsert(entity);
        }
    }

    @Override
    public boolean requiresPostCommitHanding(EntityPersister persister) {
        return false;
    }

    public void onPostUpdate(PostUpdateEvent event) {
        Object entity = event.getEntity();
        EntityPersister entityPersister = event.getPersister();
        Class<?> clazz = ClassUtil.forName(entityPersister.getRootEntityName());
        if (IndexChecker.hasIndexed(clazz)) {
            EntityChangedListener luceneListener = DaoCache.getInstance().get(clazz).getLuceneListener();
            if (luceneListener != null) {
                luceneListener.entityUpdate(entity);
            }
        } else if (IndexChecker.needListener(clazz)) {
            EntityChangedListener luceneListener = DaoCache.getInstance().get(clazz).getLuceneListener();
            if (luceneListener != null) {
                luceneListener.getRefListener().entityChange(clazz, event.getId().toString());
            }
        }
    }

    public void onPostDelete(PostDeleteEvent event) {
        EntityPersister entityPersister = event.getPersister();
        Class<?> clazz = ClassUtil.forName(entityPersister.getRootEntityName());
        if (IndexChecker.hasIndexed(clazz)) {
            EntityChangedListener luceneListener = DaoCache.getInstance().get(clazz).getLuceneListener();
            if (luceneListener != null) {
                luceneListener.entityRemove(event.getId().toString());
            }
        } else if (IndexChecker.needListener(clazz)) {
            EntityChangedListener luceneListener = DaoCache.getInstance().get(clazz).getLuceneListener();
            if (luceneListener != null) {
                luceneListener.getRefListener().entityChange(clazz, event.getId().toString());
            }
        }
    }

}
