package org.jfantasy.pay.bean.enums;

/**
 * 发行卡的批次状态
 */
public enum CardStatus {
    /**
     * 休眠(不能使用)
     */
    sleep,
    /**
     * 已激活(可以正常使用)
     */
    activated,
    /**
     * 已使用(被充值、或者绑定到用户)
     */
    used,
    /**
     * 已过期(超出使用期限)
     */
    expired,
    /**
     * 已失效(因为部分原因、比如取消发行、损坏等)
     */
    invalid
}
