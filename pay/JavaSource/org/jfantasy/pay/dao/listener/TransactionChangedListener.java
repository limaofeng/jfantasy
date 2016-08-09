package org.jfantasy.pay.dao.listener;

import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostUpdateEvent;
import org.jfantasy.framework.dao.hibernate.listener.AbstractChangedListener;
import org.jfantasy.pay.bean.Transaction;
import org.jfantasy.pay.event.TransactionChangedEvent;

public class TransactionChangedListener extends AbstractChangedListener<Transaction> {

    @Override
    public void onPostInsert(Transaction transaction, PostInsertEvent event) {
        getApplicationContext().publishEvent(new TransactionChangedEvent(transaction.getStatus(), transaction));
    }

    @Override
    public void onPostUpdate(Transaction transaction, PostUpdateEvent event) {
        if (modify(event, "status")) {
            getApplicationContext().publishEvent(new TransactionChangedEvent(transaction.getStatus(), transaction));
        }
    }

}
