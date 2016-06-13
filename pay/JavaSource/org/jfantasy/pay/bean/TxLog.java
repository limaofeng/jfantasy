package org.jfantasy.pay.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.pay.bean.enums.TxStatus;

import javax.persistence.*;

/**
 * 交易日志
 */
@Entity
@Table(name = "TRANSACTION_LOG",uniqueConstraints = {@UniqueConstraint(name = "UK_LOG_TRANSACTION_STATUS", columnNames = {"TX_SN", "TX_STATUS"})})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TxLog extends BaseBusEntity {

    @Id
    @Column(name = "ID", updatable = false)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 交易
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TX_SN", referencedColumnName = "SN", updatable = false)
    private Transaction transaction;
    /**
     * 状态
     */
    @Column(name = "TX_STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private TxStatus status;
    /**
     * 备注
     */
    @Column(name = "NOTES")
    private String notes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public TxStatus getStatus() {
        return status;
    }

    public void setStatus(TxStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}
