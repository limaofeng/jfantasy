package org.jfantasy.pay.event.source;

import org.jfantasy.pay.bean.Transaction;
import org.jfantasy.pay.bean.enums.TxStatus;


public class TxnSource {

    private TxStatus status;
    private Transaction transaction;

    public TxnSource(TxStatus status, Transaction transaction) {
        this.status = status;
        this.transaction = transaction;
    }

    public TxStatus getStatus() {
        return status;
    }

    public void setStatus(TxStatus status) {
        this.status = status;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}
