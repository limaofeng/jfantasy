package org.jfantasy.pay.service;

import org.jfantasy.framework.util.common.BeanUtil;
import org.jfantasy.pay.bean.PayConfig;
import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.error.PayException;
import org.jfantasy.pay.product.PayProduct;
import org.jfantasy.pay.product.PayType;
import org.jfantasy.pay.product.order.Order;
import org.jfantasy.pay.product.order.OrderService;
import org.jfantasy.pay.product.order.OrderServiceFactory;
import org.jfantasy.pay.service.vo.ToPayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Properties;

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

    public ToPayment pay(Long payConfigId, PayType payType, String orderType, String orderSn, String payer, Properties properties) throws PayException {
        //获取订单信息
        OrderService orderService = orderServiceFactory.getOrderService(orderType);
        Order order = orderService.loadOrder(orderSn);
        //获取支付配置
        PayConfig payConfig = payConfigService.get(payConfigId);
        //获取支付产品
        PayProduct payProduct = payProductConfiguration.loadPayProduct(payConfig.getPayProductId());
        //开始支付,创建支付记录
        Payment payment = paymentService.ready(order, payConfig, payProduct, payer);

        ToPayment toPayment = new ToPayment();
        BeanUtil.copyProperties(toPayment, payment);

        if (PayType.web == payType) {
            toPayment.setSource(payProduct.web(payment, order, properties));
        } else if (PayType.app == payType) {
            toPayment.setSource(payProduct.app(payment, order));
        }
        return toPayment;
    }

    public Order notify(String sn, String body) {
        Payment payment = paymentService.get(sn);

        PayConfig payConfig = payment.getPayConfig();

        //订单服务
        OrderService orderService = orderServiceFactory.getOrderService(payment.getOrderType());

        //获取支付产品
        PayProduct payProduct = payProductConfiguration.loadPayProduct(payConfig.getPayProductId());

        //支付订单
        Order order = orderService.loadOrder(payment.getOrderSn());

        //更新支付状态
        paymentService.result(payProduct.payNotify(payment, body), order);

        //返回订单信息
        return orderService.loadOrder(payment.getOrderSn());
    }

}
