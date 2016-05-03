package org.jfantasy.pay.order;

import org.jfantasy.pay.order.entity.OrderKey;
import org.jfantasy.pay.order.entity.Refund;

import java.math.BigDecimal;

/**
 * 订单处理器
 */
public interface OrderProcessor {

    /**
     * 发起退款操作
     *
     * @param key    订单key
     * @param amount 退款金额
     * @param remark 描述
     * @return Refund
     */
    Refund refund(OrderKey key, BigDecimal amount, String remark);

}
