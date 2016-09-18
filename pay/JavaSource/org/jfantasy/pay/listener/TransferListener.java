package org.jfantasy.pay.listener;

import org.jfantasy.pay.bean.Account;
import org.jfantasy.pay.bean.Transaction;
import org.jfantasy.pay.bean.enums.ProjectType;
import org.jfantasy.pay.bean.enums.TxStatus;
import org.jfantasy.pay.event.TransactionChangedEvent;
import org.jfantasy.pay.service.AccountService;
import org.jfantasy.pay.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
public class TransferListener implements ApplicationListener<TransactionChangedEvent> {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private AccountService accountService;

    @Override
    @Async
    @Transactional
    public void onApplicationEvent(TransactionChangedEvent event) {
        Transaction transaction = event.getTransaction();
        if (transaction.getProject().getType() == ProjectType.transfer && transaction.getStatus() == TxStatus.unprocessed) {
            transfer(transaction.getSn(), transaction.getFrom(), transaction.getTo(), transaction.getAmount());
        }
    }

    /**
     * 转账 TODO 不考虑账户余额不足的问题
     *
     * @param from   转出
     * @param to     转入
     * @param amount 金额
     * @return Transaction
     */
    private Transaction transfer(String sn, String from, String to, BigDecimal amount) {
        Transaction transaction = this.transactionService.get(sn);
        transaction.setStatus(TxStatus.processing);
        Account _from = accountService.get(from);
        Account _to = accountService.get(to);
        _from.setAmount(_from.getAmount().subtract(amount));
        _to.setAmount(_to.getAmount().add(amount));
        transaction.setStatus(TxStatus.success);
        return transactionService.save(transaction);
    }

}
