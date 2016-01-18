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

    public ToPayment pay(Long payConfigId, PayType payType, String orderType, String orderSn, String payer) throws PayException {
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
        BeanUtil.copyProperties(toPayment,payment);

        if (PayType.web == payType) {
            toPayment.setSource(payProduct.web(order, payment));
        } else if (PayType.app == payType) {
            toPayment.setSource(payProduct.app(order, payment));
        }
        return toPayment;

    }
}
