package org.jfantasy.pay.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Restrictions;
import org.jfantasy.framework.util.common.BeanUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.pay.bean.PayConfig;
import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.bean.Refund;
import org.jfantasy.pay.error.PayException;
import org.jfantasy.pay.event.PaySuccessfulEvent;
import org.jfantasy.pay.event.context.PayContext;
import org.jfantasy.pay.product.PayProduct;
import org.jfantasy.pay.product.PayType;
import org.jfantasy.pay.product.order.Order;
import org.jfantasy.pay.product.order.OrderService;
import org.jfantasy.pay.product.order.OrderServiceFactory;
import org.jfantasy.pay.service.vo.ToPayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Properties;

/**
 * 支付服务
 */
@Service
@Transactional
public class PayService {

    private final static Log LOG = LogFactory.getLog(PayService.class);

    @Autowired
    private PayProductConfiguration payProductConfiguration;
    @Autowired
    private PayConfigService payConfigService;
    @Autowired
    private OrderServiceFactory orderServiceFactory;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private RefundService refundService;
    @Autowired
    private ApplicationContext applicationContext;

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

    /**
     * 发起退款
     *
     * @param payment 原支付交易
     * @param amount  退款金额
     * @param remark  备注
     * @return Refund
     */
    public Refund refund(Payment payment, BigDecimal amount, String remark) {
        if (payment.getStatus() != Payment.Status.success) {
            throw new PayException("原交易[" + payment.getSn() + "]未支付成功,不能发起退款操作");
        }
        Refund refund = refundService.ready(payment, amount, remark);

        PayConfig payConfig = refund.getPayConfig();

        //获取支付产品
        PayProduct payProduct = payProductConfiguration.loadPayProduct(payConfig.getPayProductId());

        return refundService.save(payProduct.refund(refund));
    }

    public Order notify(Payment payment, String body) {
        PayConfig payConfig = payment.getPayConfig();

        //订单服务
        OrderService orderService = orderServiceFactory.getOrderService(payment.getOrderType());

        //获取支付产品
        PayProduct payProduct = payProductConfiguration.loadPayProduct(payConfig.getPayProductId());

        //支付订单
        Order order = orderService.loadOrder(payment.getOrderSn());

        //更新支付状态
        paymentService.result(payProduct.payNotify(payment, body), order);

        //推送事件
        PayContext context = new PayContext(payment, order);
        try {
            if (Payment.Status.success == payment.getStatus()) {
                this.applicationContext.publishEvent(new PaySuccessfulEvent(context));
            } else if (Payment.Status.failure == payment.getStatus()) {
                this.applicationContext.publishEvent(new PaySuccessfulEvent(context));
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

        //返回订单信息
        return orderService.loadOrder(payment.getOrderSn());
    }

    public Order notify(Refund refund, String body) {

        PayConfig payConfig = refund.getPayConfig();

        //订单服务
        OrderService orderService = orderServiceFactory.getOrderService(refund.getOrderType());

        //获取支付产品
        PayProduct payProduct = payProductConfiguration.loadPayProduct(payConfig.getPayProductId());

        //支付订单
        Order order = orderService.loadOrder(refund.getOrderSn());

        //更新状态
        refundService.result(payProduct.payNotify(refund, body), order);

        //推送事件
        PayContext context = new PayContext(refund, order);
        try {
            if (Refund.Status.success == refund.getStatus()) {
                this.applicationContext.publishEvent(new PaySuccessfulEvent(context));
            } else if (Refund.Status.failure == refund.getStatus()) {
                this.applicationContext.publishEvent(new PaySuccessfulEvent(context));
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

        //返回订单信息
        return orderService.loadOrder(refund.getOrderSn());
    }

    /**
     * 获取订单的支付记录
     *
     * @param orderKey orderType + ":" + orderSn
     * @return List<Payment>
     */
    public List<Payment> getPaymentsByOrderKey(String orderKey) {
        String[] strs = StringUtil.tokenizeToStringArray(orderKey, ":");
        String orderType = strs[0], orderSn = strs[1];
        return paymentService.find(Restrictions.eq("orderType", orderType), Restrictions.eq("orderSn", orderSn));
    }

}
