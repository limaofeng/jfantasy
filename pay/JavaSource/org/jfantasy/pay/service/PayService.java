package org.jfantasy.pay.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Restrictions;
import org.jfantasy.framework.spring.mvc.error.RestException;
import org.jfantasy.framework.util.common.BeanUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.pay.bean.Order;
import org.jfantasy.pay.bean.PayConfig;
import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.bean.Refund;
import org.jfantasy.pay.error.PayException;
import org.jfantasy.pay.event.PayNotifyEvent;
import org.jfantasy.pay.event.PayRefundNotifyEvent;
import org.jfantasy.pay.event.context.PayContext;
import org.jfantasy.pay.order.OrderServiceFactory;
import org.jfantasy.pay.order.entity.OrderDetails;
import org.jfantasy.pay.order.entity.OrderKey;
import org.jfantasy.pay.order.entity.enums.PaymentStatus;
import org.jfantasy.pay.product.PayProduct;
import org.jfantasy.pay.product.PayType;
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
    private OrderService orderService;
    @Autowired
    private ApplicationContext applicationContext;

    public ToPayment pay(Long payConfigId, PayType payType, String orderType, String orderSn, String payer, Properties properties) throws PayException {
        OrderKey key = OrderKey.newInstance(orderType,orderSn);
        Order order = orderService.get(key);
        OrderDetails orderDetails = null;
        if(order == null) {
            if(!orderServiceFactory.containsType(orderType)){
                throw new RestException("orderType[" + orderType + "] 对应的 PaymentOrderService 未配置！");
            }
            //获取订单信息
            orderDetails = orderServiceFactory.getOrderService(orderType).loadOrder(key.toString());
            if(orderDetails == null){
                throw new RestException("order = [" + key + "] 不存在,请核对后,再继续操作!");
            }
            order = orderService.save(orderDetails);
        }else {
            orderDetails = orderServiceFactory.getOrderService(orderType).loadOrder(key.toString());
        }
        if (!orderDetails.isPayment()) {
            throw new RestException("业务系统异常,不能继续支付");
        }
        //获取支付配置
        PayConfig payConfig = payConfigService.get(payConfigId);
        //获取支付产品
        PayProduct payProduct = payProductConfiguration.loadPayProduct(payConfig.getPayProductId());
        //开始支付,创建支付记录
        Payment payment = paymentService.ready(order, payConfig, payProduct, payer);

        ToPayment toPayment = new ToPayment();
        BeanUtil.copyProperties(toPayment, payment, "status", "type");
        toPayment.setStatus(payment.getStatus());
        toPayment.setType(payment.getType());

        if (PayType.web == payType) {
            toPayment.setSource(payProduct.web(payment, order, properties));
        } else if (PayType.app == payType) {
            toPayment.setSource(payProduct.app(payment, order, properties));
        }
        //保存支付信息
        paymentService.save(payment);
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
        Refund refund = refundService.ready(payment, amount, remark);

        PayConfig payConfig = refund.getPayConfig();

        //获取支付产品
        PayProduct payProduct = payProductConfiguration.loadPayProduct(payConfig.getPayProductId());

        return refundService.save(payProduct.refund(refund));
    }

    public Object notify(Payment payment, String body) {
        PayConfig payConfig = payment.getPayConfig();

        //获取支付产品
        PayProduct payProduct = payProductConfiguration.loadPayProduct(payConfig.getPayProductId());

        //支付订单
        Order order = payment.getOrder();

        PaymentStatus oldStatus = payment.getStatus();

        Object result = payProduct.payNotify(payment, body);

        //支付状态未发生变化
        if (payment.getStatus() == oldStatus) {
            return result == null ? order : result;
        }

        //更新支付状态
        paymentService.save(payment);

        // 如果为完成 或者 初始状态 不触发事件
        if (payment.getStatus() == PaymentStatus.finished || payment.getStatus() == PaymentStatus.ready) {
            return result == null ? order : result;
        }

        //推送事件
        PayContext context = new PayContext(payment, order);
        try {
            this.applicationContext.publishEvent(new PayNotifyEvent(context));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

        //返回订单信息
        return result != null ? result : order;
    }

    public Object notify(Refund refund, String body) {

        PayConfig payConfig = refund.getPayConfig();

        //获取支付产品
        PayProduct payProduct = payProductConfiguration.loadPayProduct(payConfig.getPayProductId());

        //支付订单
        Order order = refund.getOrder();

        Object result = payProduct.payNotify(refund, body);

        //更新状态
        refundService.result(refund, order);

        //推送事件
        PayContext context = new PayContext(refund, order);
        try {
            this.applicationContext.publishEvent(new PayRefundNotifyEvent(context));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

        //返回订单信息
        return result != null ? result : order;
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