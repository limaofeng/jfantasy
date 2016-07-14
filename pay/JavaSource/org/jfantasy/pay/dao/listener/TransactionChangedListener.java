package org.jfantasy.pay.dao.listener;

import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.jfantasy.framework.dao.hibernate.listener.AbstractChangedListener;
import org.jfantasy.pay.bean.Transaction;
import org.jfantasy.pay.event.TransactionChangedEvent;


public class TransactionChangedListener extends AbstractChangedListener<Transaction> implements PostInsertEventListener, PostUpdateEventListener {

    @Override
    public void onPostInsert(PostInsertEvent event) {
        if (missing(event)) {
            return;
        }
        Transaction transaction = getEntity(event);
        getApplicationContext().publishEvent(new TransactionChangedEvent(transaction.getStatus(),transaction));
    }

    @Override
    public void onPostUpdate(PostUpdateEvent event) {
        if (missing(event)) {
            return;
        }
        Transaction transaction = getEntity(event);
        if(modify(event,"status")) {
            getApplicationContext().publishEvent(new TransactionChangedEvent(transaction.getStatus(), transaction));
        }
    }

}
