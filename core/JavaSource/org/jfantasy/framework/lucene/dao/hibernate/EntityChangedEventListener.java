package org.jfantasy.framework.lucene.dao.hibernate;

import org.jfantasy.framework.lucene.BuguIndex;
import org.jfantasy.framework.lucene.backend.EntityChangedListener;
import org.jfantasy.framework.lucene.backend.IndexChecker;
import org.jfantasy.framework.lucene.cache.DaoCache;
import org.jfantasy.framework.lucene.dao.LuceneDao;
import org.jfantasy.framework.util.common.ClassUtil;
import org.hibernate.event.spi.*;
import org.hibernate.persister.entity.EntityPersister;

public class EntityChangedEventListener implements PostInsertEventListener, PostUpdateEventListener, PostDeleteEventListener {

    private static final long serialVersionUID = -4339024045294333782L;

    public void onPostInsert(PostInsertEvent event) {
        if(!BuguIndex.isRunning()){
            return;
        }

        Object entity = event.getEntity();
        EntityPersister entityPersister = event.getPersister();
        Class<?> clazz = ClassUtil.forName(entityPersister.getRootEntityName());
        LuceneDao luceneDao = DaoCache.getInstance().get(clazz);
        if (luceneDao == null) {
            return;
        }
        EntityChangedListener luceneListener = DaoCache.getInstance().get(clazz).getLuceneListener();
        if (luceneListener == null) {
            return;
        }
        if (!IndexChecker.hasIndexed(clazz)) {
            return;
        }
        luceneListener.entityInsert(entity);
    }

    @Override
    public boolean requiresPostCommitHanding(EntityPersister persister) {
        return false;
    }

    public void onPostUpdate(PostUpdateEvent event) {
        if(!BuguIndex.isRunning()){
            return;
        }

        Object entity = event.getEntity();
        EntityPersister entityPersister = event.getPersister();
        Class<?> clazz = ClassUtil.forName(entityPersister.getRootEntityName());
        LuceneDao luceneDao = DaoCache.getInstance().get(clazz);
        if (luceneDao == null) {
            return;
        }
        EntityChangedListener luceneListener = DaoCache.getInstance().get(clazz).getLuceneListener();
        if (luceneListener == null) {
            return;
        }
        if (IndexChecker.hasIndexed(clazz)) {
            luceneListener.entityUpdate(entity);
        } else if (IndexChecker.needListener(clazz)) {
            luceneListener.getRefListener().entityChange(clazz, event.getId().toString());
        }
    }

    public void onPostDelete(PostDeleteEvent event) {
        if(!BuguIndex.isRunning()){
            return;
        }

        EntityPersister entityPersister = event.getPersister();
        Class<?> clazz = ClassUtil.forName(entityPersister.getRootEntityName());
        LuceneDao luceneDao = DaoCache.getInstance().get(clazz);
        if (luceneDao == null) {
            return;
        }
        EntityChangedListener luceneListener = DaoCache.getInstance().get(clazz).getLuceneListener();
        if (luceneListener == null) {
            return;
        }
        if (IndexChecker.hasIndexed(clazz)) {
            luceneListener.entityRemove(event.getId().toString());
        } else if (IndexChecker.needListener(clazz)) {
            luceneListener.getRefListener().entityChange(clazz, event.getId().toString());
        }
    }

}
