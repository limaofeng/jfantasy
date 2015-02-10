package com.fantasy.swp.listener;


import com.fantasy.framework.util.common.ObjectUtil;
import org.hibernate.event.spi.*;
import org.hibernate.persister.entity.EntityPersister;

public class EntityChangedEventListener implements PostInsertEventListener, PostUpdateEventListener, PostDeleteEventListener {

    @Override
    public void onPostDelete(PostDeleteEvent event) {

    }

    @Override
    public void onPostInsert(PostInsertEvent event) {

    }


    public boolean modify(PostUpdateEvent event,String property) {
//        Arrays.binarySearch(event.getPersister().getPropertyNames(),property);
        int index = ObjectUtil.indexOf(event.getPersister().getPropertyNames(), property);
        return index != -1 && event.getState()[index].equals(event.getOldState()[index]);
    }

    @Override
    public void onPostUpdate(PostUpdateEvent event) {



    }

    @Override
    public boolean requiresPostCommitHanding(EntityPersister persister) {
        return false;
    }
}
