package org.jfantasy.pay.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.pay.bean.enums.TxStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 交易日志
 */
@Entity
@Table(name = "PAY_TRANSACTION_LOG")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TxLog extends BaseBusEntity {

    private Long id;
    /**
     * 交易
     */
    private Transaction transaction;
    /**
     * 状态
     */
    @Column(name = "STATUS", nullable = false)
    private TxStatus status;
    /**
     * 备注
     */
    @Column(name = "NOTES")
    private String notes;


}
