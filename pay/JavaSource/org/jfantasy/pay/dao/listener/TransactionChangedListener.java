package org.jfantasy.pay.dao.listener;

import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.jfantasy.framework.dao.hibernate.listener.AbstractChangedListener;
import org.jfantasy.framework.spring.SpringContextUtil;
import org.jfantasy.framework.util.common.ClassUtil;
import org.jfantasy.pay.bean.Transaction;
import org.jfantasy.pay.event.TransactionChangedEvent;
import org.springframework.context.ApplicationContext;


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

    @Override
    public boolean requiresPostCommitHanding(EntityPersister persister) {
        Class<?> aClass = ClassUtil.forName(persister.getRootEntityName());
        assert aClass != null;
        return Transaction.class.isAssignableFrom(aClass);
    }

    private ApplicationContext applicationContext;

    private ApplicationContext getApplicationContext(){
        if(applicationContext == null){
            return this.applicationContext = SpringContextUtil.getApplicationContext();
        }
        return this.applicationContext;
    }

}
