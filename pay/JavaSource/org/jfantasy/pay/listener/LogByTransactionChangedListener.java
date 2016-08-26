package org.jfantasy.pay.listener;

import org.jfantasy.pay.bean.Transaction;
import org.jfantasy.pay.bean.enums.OwnerType;
import org.jfantasy.pay.event.TransactionChangedEvent;
import org.jfantasy.pay.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 记录交易日志
 */
@Component
public class LogByTransactionChangedListener implements ApplicationListener<TransactionChangedEvent> {

    private final LogService logService;

    @Autowired
    public LogByTransactionChangedListener(LogService logService) {
        this.logService = logService;
    }

    @Override
    @Async
    public void onApplicationEvent(TransactionChangedEvent event) {
        Transaction transaction = event.getTransaction();
        logService.log(OwnerType.transaction,transaction.getSn(),transaction.getStatus().name(),transaction.getNotes());
    }

}
