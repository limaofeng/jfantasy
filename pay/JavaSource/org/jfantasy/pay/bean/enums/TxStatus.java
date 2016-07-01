package org.jfantasy.pay.bean.enums;

/**
 * 交易状态
 */
public enum TxStatus {
    /**
     * 未处理
     */
    unprocessed,
    /**
     * 处理中
     */
    processing,
    /**
     * 成功
     */
    success,
    /**
     * 关闭
     */
    close;
}
