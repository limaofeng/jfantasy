package org.jfantasy.pay.order;

/**
 * 服务接入方需要先通过该接口,注册服务
 */
public interface OrderServiceRegistry {

    /**
     * 向支付中心注册 OrderService
     *
     * @param type 处理的订单类型
     * @param host 地址
     * @param port 端口
     */
    void register(String type, String host, int port);

    /**
     * 向支付中心注册 OrderService
     *
     * @param types 处理的订单类型
     * @param host  地址
     * @param port  端口
     */
    void register(String[] types, String host, int port);

}
