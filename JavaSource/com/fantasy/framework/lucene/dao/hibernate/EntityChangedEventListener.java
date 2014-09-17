package com.fantasy.framework.lucene.dao.hibernate;

import com.fantasy.framework.lucene.backend.EntityChangedListener;
import com.fantasy.framework.lucene.backend.IndexChecker;
import com.fantasy.framework.util.common.ClassUtil;
import org.hibernate.event.spi.*;
import org.hibernate.persister.entity.EntityPersister;

import java.util.HashMap;
import java.util.Map;

public class EntityChangedEventListener implements PostInsertEventListener, PostUpdateEventListener, PostDeleteEventListener {

	private static final long serialVersionUID = -4339024045294333782L;
	private Map<Class<?>, EntityChangedListener> entityChangeds = new HashMap<Class<?>, EntityChangedListener>();

	private EntityChangedListener getEntityChangedListener(Class<?> clazz) {
		if (!entityChangeds.containsKey(clazz)) {
			entityChangeds.put(clazz, new EntityChangedListener(clazz));
		}
		return entityChangeds.get(clazz);
	}

	public void onPostInsert(PostInsertEvent event) {
		Object entity = event.getEntity();
		EntityPersister entityPersister = event.getPersister();
		Class<?> clazz = ClassUtil.forName(entityPersister.getRootEntityName());
		if (!IndexChecker.hasIndexed(clazz))
			return;
		getEntityChangedListener(clazz).entityInsert(entity);
	}

    @Override
    public boolean requiresPostCommitHanding(EntityPersister persister) {
        return false;
    }

    public void onPostUpdate(PostUpdateEvent event) {
		Object entity = event.getEntity();
		EntityPersister entityPersister = event.getPersister();
		Class<?> clazz = ClassUtil.forName(entityPersister.getRootEntityName());
		if (IndexChecker.hasIndexed(clazz))
			getEntityChangedListener(clazz).entityUpdate(entity);
		else if (IndexChecker.needListener(clazz))
			getEntityChangedListener(clazz).getRefListener().entityChange(clazz, event.getId().toString());
	}

	public void onPostDelete(PostDeleteEvent event) {
		EntityPersister entityPersister = event.getPersister();
		Class<?> clazz = ClassUtil.forName(entityPersister.getRootEntityName());
		if (IndexChecker.hasIndexed(clazz))
			getEntityChangedListener(clazz).entityRemove(event.getId().toString());
		else if (IndexChecker.needListener(clazz))
			getEntityChangedListener(clazz).getRefListener().entityChange(clazz, event.getId().toString());
	}

}
