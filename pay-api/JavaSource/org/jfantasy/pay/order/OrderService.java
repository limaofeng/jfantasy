package org.jfantasy.pay.order;


import org.jfantasy.pay.order.entity.enums.PaymentStatus;
import org.jfantasy.pay.order.entity.enums.RefundStatus;
import org.jfantasy.pay.order.entity.OrderDetails;

/**
 * 支付订单接口<br/>
 * 服务接入放需要实现该接口
 */
public interface OrderService {

    /**
     * 订单类型
     *
     * @return String
     */
    String[] types();

    /**
     * 查询订单信息
     *
     * @param key 订单Key <br/>
     *            格式为: "type:sn" <br/>
     *            样例: "test:t000001"
     * @return OrderDetails
     */
    OrderDetails loadOrder(String key);

    /**
     * 支付事件
     *
     * @param key     订单Key
     * @param status  状态
     * @param message 消息
     */
    void on(String key, PaymentStatus status, String message);

    /**
     * 退款事件
     *
     * @param key     订单Key
     * @param status  状态
     * @param message 消息
     */
    void on(String key, RefundStatus status, String message);

}
