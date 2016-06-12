package org.jfantasy.pay.bean;

import org.jfantasy.framework.dao.BaseBusEntity;

import java.math.BigDecimal;

public class Account extends BaseBusEntity {

    enum Type {
        /**
         * 个人账户
         */
        personal,
        /**
         * 企业账户
         */
        enterprise
    }

    /**
     * 账户余额
     */
    private BigDecimal balance;

    /**
     * 编号
     */
    private String sn;
    /**
     * 账号类型
     */
    private Type type;
    /**
     * 所有者
     */
    private String owner;
}
