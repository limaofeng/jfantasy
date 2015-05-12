package com.fantasy.file.listener;

import com.fantasy.file.bean.FileManagerConfig;
import com.fantasy.file.service.FileManagerFactory;
import com.fantasy.framework.util.common.ObjectUtil;
import org.hibernate.event.spi.*;
import org.hibernate.persister.entity.EntityPersister;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

public class FileManagerEventListener implements PostInsertEventListener, PostUpdateEventListener, PostDeleteEventListener {

	private static final long serialVersionUID = 1082020263270806626L;

	@Autowired
	private FileManagerFactory factory;

    public static boolean hasModified(PostUpdateEvent event,String property) {
        Arrays.binarySearch(event.getPersister().getPropertyNames(), property);
        int index = ObjectUtil.indexOf(event.getPersister().getPropertyNames(), property);
        return index != -1 && event.getState()[index].equals(event.getOldState()[index]);
    }

	@Override
	public void onPostDelete(PostDeleteEvent event) {
		if (event.getEntity() instanceof FileManagerConfig) {
			factory.remove((FileManagerConfig)event.getEntity());
		}
	}

	@Override
	public void onPostUpdate(PostUpdateEvent event) {
		if (event.getEntity() instanceof FileManagerConfig) {
            FileManagerConfig config = (FileManagerConfig)event.getEntity();
            if(hasModified(event,"configParamStore")) {
                factory.registerFileManager(config.getId(), config.getType(), config.getConfigParams());
            }
		}
	}

	@Override
	public void onPostInsert(PostInsertEvent event) {
		if (event.getEntity() instanceof FileManagerConfig) {
            FileManagerConfig config = (FileManagerConfig)event.getEntity();
            factory.registerFileManager(config.getId(), config.getType(), config.getConfigParams());
		}
	}

    @Override
    public boolean requiresPostCommitHanding(EntityPersister persister) {
        return false;
    }

}
