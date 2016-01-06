package org.jfantasy.framework.dao.hibernate.event;


import org.hibernate.event.spi.PostUpdateEvent;

public abstract class EntityChangeUtil {

    private EntityChangeUtil() {
    }

    public static boolean modified(PostUpdateEvent event, String propertyName) {
        Object[] currentState = event.getState();
        String[] propertyNames = event.getPersister().getPropertyNames();
        Object[] oldState = event.getOldState();
        for (int i = 0; i < propertyNames.length; i++) {
            if (propertyNames[i].equals(propertyName)) {
                return currentState[i] != null && !currentState[i].equals(oldState[i]);
            }
        }
        return false;
    }

}
