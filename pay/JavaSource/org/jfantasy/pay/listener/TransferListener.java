package org.jfantasy.pay.listener;

import org.jfantasy.pay.event.TransactionChangedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class TransferListener implements ApplicationListener<TransactionChangedEvent> {

    @Override
    public void onApplicationEvent(TransactionChangedEvent event) {

    }

}
