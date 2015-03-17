package com.fantasy.file.listener;

import com.fantasy.file.bean.FileManagerConfig;
import com.fantasy.file.service.FileManagerFactory;
import org.hibernate.event.spi.*;
import org.hibernate.persister.entity.EntityPersister;

import javax.annotation.Resource;

public class FileManagerEventListener implements PostInsertEventListener, PostUpdateEventListener, PostDeleteEventListener {

	private static final long serialVersionUID = 1082020263270806626L;

	@Autowired
	private FileManagerFactory factory;
	
	@Override
	public void onPostDelete(PostDeleteEvent event) {
		if (event.getEntity() instanceof FileManagerConfig) {
			factory.remove((FileManagerConfig)event.getEntity());
		}
	}

	@Override
	public void onPostUpdate(PostUpdateEvent event) {
		if (event.getEntity() instanceof FileManagerConfig) {
			factory.initialize((FileManagerConfig)event.getEntity());
		}
	}

	@Override
	public void onPostInsert(PostInsertEvent event) {
		if (event.getEntity() instanceof FileManagerConfig) {
			factory.initialize((FileManagerConfig)event.getEntity());
		}
	}

    @Override
    public boolean requiresPostCommitHanding(EntityPersister persister) {
        return false;
    }

}
