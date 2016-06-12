package org.jfantasy.pay.bean;

import org.jfantasy.framework.dao.BaseBusEntity;

import java.math.BigDecimal;

/**
 * 交易表
 */
public class Transaction extends BaseBusEntity {
    /**
     * 交易流水号
     */
    private String sn;
    /**
     * 转出账号
     */
    private String from;
    /**
     * 转入账号
     */
    private String to;
    /**
     * 金额
     */
    private BigDecimal amount;

}
