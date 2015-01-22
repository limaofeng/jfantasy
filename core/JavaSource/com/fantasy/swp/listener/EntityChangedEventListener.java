package com.fantasy.swp.listener;


import org.hibernate.event.spi.*;
import org.hibernate.persister.entity.EntityPersister;

public class EntityChangedEventListener implements PostInsertEventListener, PostUpdateEventListener, PostDeleteEventListener {

    @Override
    public void onPostDelete(PostDeleteEvent event) {

    }

    @Override
    public void onPostInsert(PostInsertEvent event) {

    }

    @Override
    public void onPostUpdate(PostUpdateEvent event) {

    }

    @Override
    public boolean requiresPostCommitHanding(EntityPersister persister) {
        return false;
    }
}
