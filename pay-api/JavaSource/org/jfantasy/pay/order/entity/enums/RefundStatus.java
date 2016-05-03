package org.jfantasy.pay.order.entity.enums;

public enum RefundStatus {
    /**
     * 准备 (下一步状态 为 关闭/退款中)
     */
    ready,
    /**
     * 关闭(最终状态 , 不可继续操作)
     */
    close,
    /**
     * 退款中
     */
    wait,
    /**
     * 退款成功(最终状态 , 不可继续操作)
     */
    success,
    /**
     * 退款失败(最终状态 , 不可继续操作)
     */
    failure
}