package org.jfantasy.pay.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.jfantasy.framework.spring.mvc.error.ValidationException;
import org.jfantasy.framework.util.common.BeanUtil;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.pay.bean.*;
import org.jfantasy.pay.bean.enums.ProjectType;
import org.jfantasy.pay.error.PayException;
import org.jfantasy.pay.event.PayNotifyEvent;
import org.jfantasy.pay.event.PayRefundNotifyEvent;
import org.jfantasy.pay.event.PayStatusEvent;
import org.jfantasy.pay.event.context.PayContext;
import org.jfantasy.pay.event.context.PayStatus;
import org.jfantasy.pay.order.entity.OrderDetails;
import org.jfantasy.pay.order.entity.OrderKey;
import org.jfantasy.pay.order.entity.enums.PaymentStatus;
import org.jfantasy.pay.order.entity.enums.PaymentType;
import org.jfantasy.pay.order.entity.enums.RefundStatus;
import org.jfantasy.pay.product.PayProduct;
import org.jfantasy.pay.product.PayType;
import org.jfantasy.pay.service.vo.ToPayment;
import org.jfantasy.pay.service.vo.ToRefund;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Properties;

/**
 * 支付服务
 */
@Service
public class PayService {

    private final static Log LOG = LogFactory.getLog(PayService.class);

    private final PayProductConfiguration payProductConfiguration;
    private final PayConfigService payConfigService;
    private final PaymentService paymentService;
    private final RefundService refundService;
    private final OrderService orderService;
    private final ApplicationContext applicationContext;

    @Autowired
    public PayService(RefundService refundService, PaymentService paymentService, PayProductConfiguration payProductConfiguration, ApplicationContext applicationContext, PayConfigService payConfigService, OrderService orderService) {
        this.refundService = refundService;
        this.paymentService = paymentService;
        this.payProductConfiguration = payProductConfiguration;
        this.applicationContext = applicationContext;
        this.payConfigService = payConfigService;
        this.orderService = orderService;
    }

    /**
     * 生成预支付单与web支付表单
     *
     * @param transaction 交易对象
     * @param payConfigId 支付ID
     * @param payType     支付类型
     * @param payer       支付人
     * @param properties  支付参数
     * @return ToPayment
     * @throws PayException 支付异常
     */
    @Transactional
    public ToPayment pay(Transaction transaction, Long payConfigId, PayType payType, String payer, Properties properties) throws PayException {
        if (transaction.getProject().getType() != ProjectType.order) {
            throw new ValidationException(000.0f, "项目类型为 order 才能调用支付接口");
        }
        String orderKey = transaction.get("order_key");
        //验证业务订单
        OrderKey key = OrderKey.newInstance(orderKey);
        Order order = orderService.getOrder(key);
        OrderDetails orderDetails = order.getDetails();
        if (order.getStatus() != Order.PaymentStatus.unpaid) {
            throw new ValidationException(000.0f, "订单状态为[" + order.getStatus().getValue() + "],不满足付款的必要条件");
        }
        if (!orderDetails.isPayment()) {
            throw new ValidationException(000.0f, "业务系统异常,不能继续支付");
        }
        //获取支付配置
        PayConfig payConfig = payConfigService.get(payConfigId);
        //获取支付产品
        PayProduct payProduct = payProductConfiguration.loadPayProduct(payConfig.getPayProductId());
        //开始支付,创建支付记录
        Payment payment = paymentService.create(transaction, order, payConfig, payProduct, payer);
        //克隆返回结果
        ToPayment toPayment = new ToPayment();
        BeanUtil.copyProperties(toPayment, payment, "status", "type");
        toPayment.setStatus(payment.getStatus());
        toPayment.setType(payment.getType());
        //调用第三方支付产品
        if (PayType.web == payType) {
            toPayment.setSource(payProduct.web(payment, order, properties));
        } else if (PayType.app == payType) {
            toPayment.setSource(payProduct.app(payment, order, properties));
        }
        //有可能调用支付产品时,修改了支付状态,保存支付信息
        paymentService.save(payment);
        return toPayment;
    }

    /**
     * 发起退款
     *
     * @param paymentSn 原支付交易
     * @param amount    退款金额
     * @param remark    备注
     * @return Refund
     */
    @Transactional
    public Refund refund(String paymentSn, BigDecimal amount, String remark) {
        Refund refund = refundService.ready(paymentService.get(paymentSn), amount, remark);
        Hibernate.initialize(refund.getPayConfig());
        Hibernate.initialize(refund.getOrder());
        return refund;
    }

    /**
     * 更新退款状态 <br/>
     * 对应的业务操作: <br/>
     * ready => close
     * ready => wait
     *
     * @param sn     退款编号
     * @param status 状态
     * @param remark 备注
     * @return Refund
     */
    @Transactional
    public ToRefund refund(String sn, RefundStatus status, String remark) {
        Refund refund = refundService.get(sn);
        if (refund.getType() == PaymentType.online) {
            if (refund.getStatus() != RefundStatus.ready) {
                throw new PayException("退款状态为:" + refund.getStatus() + ",不能进行操作");
            } else if (!ObjectUtil.exists(new RefundStatus[]{RefundStatus.close, RefundStatus.wait}, status)) {
                throw new PayException("不能手动将退款状态调整为:" + status);
            }
            if (status == RefundStatus.wait) {
                PayConfig payConfig = refund.getPayConfig();
                //获取支付产品
                PayProduct payProduct = payProductConfiguration.loadPayProduct(payConfig.getPayProductId());
                Object result = payProduct.refund(refund);
                this.refundService.save(refund);
                ToRefund toRefund = BeanUtil.copyProperties(new ToRefund(), refund);
                toRefund.setSource(result);
                if (refund.getStatus() != RefundStatus.ready) {//不为 ready 时,推送事件
                    PayContext context = new PayContext(refund, refund.getOrder());
                    this.applicationContext.publishEvent(new PayRefundNotifyEvent(context));
                }
                return toRefund;
            } else if (status == RefundStatus.close) {
                refund.setStatus(status);
                this.refundService.save(refund);
                return BeanUtil.copyProperties(new ToRefund(), refund);
            } else {
                throw new PayException(" 变更退款状态到 " + status + "功能,暂未实现! 请联系技术人员. ");
            }
        } else {
            throw new PayException(" 线下退款方式,暂未实现! 请联系技术人员. ");
        }
    }

    public boolean query(String sn) {
        Payment payment = this.paymentService.get(sn);
        PayConfig payConfig = payment.getPayConfig();
        //获取支付产品
        PayProduct payProduct = payProductConfiguration.loadPayProduct(payConfig.getPayProductId());
        payProduct.query(payment);
        return false;
    }

    /**
     * 支付通知
     *
     * @param sn   支付对象
     * @param body 请求字符串
     * @return Object
     */
    @Transactional
    public Object paymentNotify(String sn, String body) {
        Payment payment = this.paymentService.get(sn);
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
        this.applicationContext.publishEvent(new PayStatusEvent(new PayStatus(payment.getStatus(), payment, order)));

        // 更新订单状态
        switch (payment.getStatus()) {
            case close:
                break;
            case success:
                order.setStatus(Order.PaymentStatus.paid);
                order.setPaymentTime(payment.getTradeTime());
                orderService.update(order);
                break;
            case finished:
                break;
            case failure:
                break;
        }

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

    /**
     * 退款通知
     *
     * @param sn   退款订单
     * @param body 请求字符串
     * @return Object
     */
    @Transactional
    public Object refundNotify(String sn, String body) {
        Refund refund = this.refundService.get(sn);
        PayConfig payConfig = refund.getPayConfig();

        //获取支付产品
        PayProduct payProduct = payProductConfiguration.loadPayProduct(payConfig.getPayProductId());

        //支付订单
        Order order = refund.getOrder();

        RefundStatus oldStatus = refund.getStatus();

        Object result = payProduct.payNotify(refund, body);

        //状态未发生变化
        if (refund.getStatus() == oldStatus) {
            return result != null ? result : order;
        }

        //更新状态
        refundService.result(refund, order);

        // 更新订单状态
        if (refund.getStatus() == RefundStatus.success) {
            order.setStatus(order.getPayableFee().equals(refund.getTotalAmount()) ? Order.PaymentStatus.refunded : Order.PaymentStatus.partRefund);
            order.setRefundAmount(refund.getTotalAmount());
            order.setRefundTime(refund.getTradeTime());
            orderService.update(order);
        }

        // 如果为完成 或者 初始状态 不触发事件
        if (refund.getStatus() == RefundStatus.wait || refund.getStatus() == RefundStatus.ready) {
            return result == null ? order : result;
        }

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

}