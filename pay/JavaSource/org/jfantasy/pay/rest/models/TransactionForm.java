package org.jfantasy.pay.rest.models;

import java.math.BigDecimal;

public class TransactionForm {
    /**
     * 转出账户
     */
    private String from;
    /**
     * 转入账户
     */
    private String to;
    /**
     * 金额
     */
    private BigDecimal amount;
    /**
     * 备注
     */
    private String notes;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
