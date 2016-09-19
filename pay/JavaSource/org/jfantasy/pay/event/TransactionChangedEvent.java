package org.jfantasy.pay.event;


import org.jfantasy.pay.bean.Transaction;
import org.jfantasy.pay.bean.enums.TxStatus;
import org.jfantasy.pay.event.source.TxnSource;
import org.springframework.context.ApplicationEvent;

public class TransactionChangedEvent extends ApplicationEvent {

    private static final long serialVersionUID = -1369840597253827146L;

    public TransactionChangedEvent(TxStatus status, Transaction transaction) {
        super(new TxnSource(status,transaction));
    }

    public TxStatus getStatus() {
        return ((TxnSource)this.getSource()).getStatus();
    }

    public Transaction getTransaction() {
        return ((TxnSource)this.getSource()).getTransaction();
    }
}
