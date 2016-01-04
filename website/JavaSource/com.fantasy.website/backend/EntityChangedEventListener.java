package org.jfantasy.website.backend;

import org.hibernate.persister.entity.EntityPersister;
import org.jfantasy.framework.util.common.ClassUtil;

//eventInterfaceFromType.put("post-commit-update", PostUpdateEventListener.class);  
//eventInterfaceFromType.put("post-commit-delete", PostDeleteEventListener.class);  
//eventInterfaceFromType.put("post-commit-insert", PostInsertEventListener.class); 
public class EntityChangedEventListener implements PostUpdateEventListener, PostDeleteEventListener, PostInsertEventListener {

	private static final long serialVersionUID = -876655770518687417L;

	public void onPostUpdate(PostUpdateEvent event) {
		Object entity = event.getEntity();
		EntityPersister entityPersister = event.getPersister();
		Class<?> clazz = ClassUtil.forName(entityPersister.getRootEntityName());
		ListenerManager.getInstance(clazz).entityUpdate(entity);
	}

    @Override
    public boolean requiresPostCommitHanding(EntityPersister persister) {
        return false;
    }

    public void onPostDelete(PostDeleteEvent event) {
		Object entity = event.getEntity();
		EntityPersister entityPersister = event.getPersister();
		Class<?> clazz = ClassUtil.forName(entityPersister.getRootEntityName());
		ListenerManager.getInstance(clazz).entityDelete(entity);
	}

	public void onPostInsert(PostInsertEvent event) {
		Object entity = event.getEntity();
		EntityPersister entityPersister = event.getPersister();
		Class<?> clazz = ClassUtil.forName(entityPersister.getRootEntityName());
		ListenerManager.getInstance(clazz).entityInsert(entity);
	}

}
