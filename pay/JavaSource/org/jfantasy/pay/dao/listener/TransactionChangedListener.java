package org.jfantasy.pay.dao.listener;

import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostUpdateEvent;
import org.jfantasy.framework.dao.annotations.EventListener;
import org.jfantasy.framework.dao.hibernate.listener.AbstractChangedListener;
import org.jfantasy.pay.bean.Transaction;
import org.jfantasy.pay.event.TransactionChangedEvent;
import org.springframework.stereotype.Component;

@Component
@EventListener
public class TransactionChangedListener extends AbstractChangedListener<Transaction> {

    private static final long serialVersionUID = 6486933157808350841L;

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
