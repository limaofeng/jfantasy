package org.jfantasy.pay.bean;

import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.Id;

/**
 * 交易记录表
 */
public class Transaction extends BaseBusEntity {

    @Id
    private Long id;

}
