package org.jfantasy.pay.order;

/**
 * 服务接入方需要先通过该接口,注册服务
 */
public interface OrderServiceRegistry {

    /**
     * 向支付中心注册 OrderService
     *
     * @param type       处理的订单类型
     * @param title       接入方名称
     * @param description 接入方描述
     * @param host        服务地址
     * @param port        服务IP
     */
    void register(String type, String title, String description, String host, int port);

    /**
     * 向支付中心注册 OrderService
     *
     * @param types       处理的订单类型
     * @param title       接入方名称
     * @param description 接入方描述
     * @param host        服务地址
     * @param port        服务IP
     */
    void register(String[] types, String title, String description, String host, int port);

}
