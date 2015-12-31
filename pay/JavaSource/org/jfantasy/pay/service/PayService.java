package org.jfantasy.pay.service;

import org.jfantasy.pay.bean.PayConfig;
import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.error.PayException;
import org.jfantasy.pay.product.PayProduct;
import org.jfantasy.pay.product.PayType;
import org.jfantasy.pay.product.order.Order;
import org.jfantasy.pay.product.order.OrderService;
import org.jfantasy.pay.product.order.OrderServiceFactory;
import org.jfantasy.pay.rest.form.PayForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 支付服务
 */
@Service
@Transactional
public class PayService {

    @Autowired
    private PayProductConfiguration payProductConfiguration;
    @Autowired
    private PayConfigService payConfigService;
    @Autowired
    private OrderServiceFactory orderServiceFactory;
    @Autowired
    private PaymentService paymentService;

    public void paying(PayForm payForm) throws PayException {
        //获取订单信息
        OrderService orderService = orderServiceFactory.getOrderService(payForm.getOrderType());
        Order order = orderService.loadOrder(payForm.getOrderSn());
        //获取支付配置
        PayConfig payConfig = payConfigService.get(payForm.getPayconfigId());
        //获取支付产品
        PayProduct payProduct = payProductConfiguration.loadPayProduct(payConfig.getPayProductId());
        //开始支付,创建支付记录
        Payment payment = paymentService.ready(order,payConfig,payProduct,payForm.getPayer());

        if(PayType.app == payForm.getPayType()){
            payProduct.app(order,payment);
        }
    }
}
