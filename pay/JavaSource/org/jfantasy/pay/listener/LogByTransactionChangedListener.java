package org.jfantasy.pay.listener;

import org.jfantasy.pay.event.TransactionChangedEvent;
import org.jfantasy.pay.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 记录交易日志
 */
@Component
public class LogByTransactionChangedListener implements ApplicationListener<TransactionChangedEvent> {

    @Autowired
    private LogService logService;

    @Override
    public void onApplicationEvent(TransactionChangedEvent event) {
        logService.log(event.getTransaction());
    }

}
