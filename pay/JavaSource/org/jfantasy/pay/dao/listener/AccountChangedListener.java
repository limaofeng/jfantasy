package org.jfantasy.pay.dao.listener;

import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.jfantasy.framework.dao.hibernate.listener.AbstractChangedListener;
import org.jfantasy.pay.bean.Account;
import org.jfantasy.pay.event.AccountChangedEvent;


public class AccountChangedListener extends AbstractChangedListener<Account> implements PostInsertEventListener, PostUpdateEventListener {

    @Override
    public void onPostInsert(PostInsertEvent event) {
        if (missing(event)) {
            return;
        }
        Account account = getEntity(event);
        getApplicationContext().publishEvent(new AccountChangedEvent(account));
    }

    @Override
    public void onPostUpdate(PostUpdateEvent event) {
        if (missing(event)) {
            return;
        }
        Account account = getEntity(event);
        if(modify(event,"amount")) {
            getApplicationContext().publishEvent(new AccountChangedEvent(account));
        }
    }

}
