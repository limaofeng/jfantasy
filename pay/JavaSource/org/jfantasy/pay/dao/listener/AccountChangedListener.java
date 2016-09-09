package org.jfantasy.pay.dao.listener;

import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostUpdateEvent;
import org.jfantasy.framework.dao.annotations.EventListener;
import org.jfantasy.framework.dao.hibernate.listener.AbstractChangedListener;
import org.jfantasy.pay.bean.Account;
import org.jfantasy.pay.event.AccountChangedEvent;
import org.springframework.stereotype.Component;

@Component
@EventListener
public class AccountChangedListener extends AbstractChangedListener<Account> {

    private static final long serialVersionUID = 6923717125278747287L;

    @Override
    public void onPostInsert(Account account, PostInsertEvent event) {
        getApplicationContext().publishEvent(new AccountChangedEvent(account));
    }

    @Override
    public void onPostUpdate(Account account, PostUpdateEvent event) {
        if (modify(event, "amount")) {
            getApplicationContext().publishEvent(new AccountChangedEvent(account));
        }
    }

}
