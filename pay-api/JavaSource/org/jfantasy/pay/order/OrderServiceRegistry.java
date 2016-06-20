package org.jfantasy.pay.order;

import org.jfantasy.pay.order.entity.enums.CallType;

import java.util.Properties;

/**
 * 服务接入方需要先通过该接口,注册服务
 */
public interface OrderServiceRegistry {

    /**
     * 向支付中心注册 OrderService
     *
     * @param type        处理的订单类型
     * @param description 接入方描述
     * @param props       接入配置
     */
    void register(CallType callType, String type, String description, Properties props);

    /**
     * 向支付中心注册 OrderService
     *
     * @param types       处理的订单类型
     * @param description 接入方描述
     * @param props       接入配置
     */
    void register(CallType callType, String[] types, String description, Properties props);

}
