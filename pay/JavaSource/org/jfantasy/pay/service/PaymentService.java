package org.jfantasy.pay.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.spring.mvc.error.NotFoundException;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.pay.bean.Order;
import org.jfantasy.pay.bean.PayConfig;
import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.dao.PaymentDao;
import org.jfantasy.pay.dao.PaymentLogDao;
import org.jfantasy.pay.error.PayException;
import org.jfantasy.pay.event.PayStatusEvent;
import org.jfantasy.pay.event.context.PayStatus;
import org.jfantasy.pay.order.entity.enums.PaymentStatus;
import org.jfantasy.pay.order.entity.enums.PaymentType;
import org.jfantasy.pay.product.Parameters;
import org.jfantasy.pay.product.PayProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;


/**
 * 支付service
 */
@Service
@Transactional
public class PaymentService {

    private final static Log LOG = LogFactory.getLog(PaymentService.class);

    @Autowired
    private PaymentDao paymentDao;
    @Autowired
    private PayConfigService payConfigService;
    @Autowired(required = false)
    private PayProductConfiguration payProductConfiguration;
    @Autowired
    private PaymentLogDao paymentLogDao;
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 支付准备
     *
     * @param order      订单信息
     * @param payConfig  支付配置
     * @param payProduct 支付产品
     * @param payer      付款人
     * @return Payment
     * @throws PayException
     */
    public Payment ready(Order order, PayConfig payConfig, PayProduct payProduct, String payer) throws PayException {
        //在线支付
        if (PayConfig.PayConfigType.online != payConfig.getPayConfigType()) {
            throw new PayException("暂时只支持在线支付");
        }

        //支付配置类型（线下支付、在线支付）
        PaymentType paymentType = PaymentType.online;
        BigDecimal paymentFee = BigDecimal.ZERO; //支付手续费

        BigDecimal amountPayable = order.getPayableFee();//应付金额（含支付手续费）

        List<Payment> payments = this.paymentDao.find(Restrictions.eq("payConfig.id", payConfig.getId()), Restrictions.eq("order.type", order.getType()), Restrictions.eq("order.sn", order.getSn()));
        // 如果存在完成订单
        Payment payment = ObjectUtil.find(payments, "status", PaymentStatus.success);
        if (payment != null && payment.getTotalAmount().subtract(payment.getPaymentFee()).compareTo(amountPayable) == 0) {//订单已支付完成
            return payment;
        }
        payment = ObjectUtil.find(payments, "status", PaymentStatus.ready);
        if (payment != null) {
            if (amountPayable.compareTo(payment.getTotalAmount().subtract(payment.getPaymentFee())) == 0) {//如果存在未完成的支付信息
                return payment;
            } else {
                this.close(payment.getSn());
            }
        }

        payment = new Payment();

        //在线支付
        String bankName = payProduct.getName();
        String bankAccount = payConfig.getBargainorId();
        payment.setType(paymentType);
        payment.setPayConfigName(payConfig.getName());
        payment.setBankName(bankName);
        payment.setBankAccount(bankAccount);
        payment.setTotalAmount(amountPayable.add(paymentFee));
        payment.setPaymentFee(paymentFee);
        payment.setPayer(payer);
        payment.setMemo(null);
        payment.setStatus(PaymentStatus.ready);
        payment.setPayConfig(payConfig);
        payment.setOrder(order);
        payment = this.paymentDao.save(payment);

        this.applicationContext.publishEvent(new PayStatusEvent(new PayStatus(payment.getStatus(),payment,order)));
        //保存交易日志
        paymentLogDao.save(payment, "创建" + payment.getPayConfigName() + " 交易");
        return payment;
    }

    public void log(Payment payment, String notes) {
        paymentLogDao.save(payment, notes);
    }

    public List<Payment> find(Criterion... criterions) {
        return this.paymentDao.find(criterions);
    }

    /**
     * 支付结果
     *
     * @param payment 支付对象
     */
    public void save(Payment payment) {
        this.paymentDao.save(payment);
        //保存交易日志
        paymentLogDao.save(payment, "创建" + payment.getPayConfigName() + " 交易");
    }

    /**
     * 过期支付单
     *
     * @param sn 支付编号
     */
    public void close(String sn) {
        this.close(sn, null);
    }

    public void close(String sn, String tradeNo) {
        Payment payment = get(sn);
        payment.setStatus(PaymentStatus.close);
        if (tradeNo != null) {
            payment.setTradeNo(tradeNo);
        }
        this.paymentDao.save(payment);
        this.paymentLogDao.save(payment, "付款" + payment.getStatus().value());
    }


    public List<Payment> find(List<PropertyFilter> filters, String orderBy, String order) {
        return this.paymentDao.find(filters, orderBy, order);
    }

    public PayConfig getPaymentConfig(Long id) {
        return this.payConfigService.get(id);
    }

    public void paynotify() {

    }

    public Pager<Payment> findPager(Pager<Payment> pager, List<PropertyFilter> filters) {
        return paymentDao.findPager(pager, filters);
    }

    public Payment get(String sn) {
        Payment payment = this.paymentDao.get(sn);
        if (payment == null) {
            throw new NotFoundException("[SN=" + sn + "]对应的支付记录未找到");
        }
        return payment;
    }

    public void delete(String... sns) {
        for (String sn : sns) {
            this.paymentDao.delete(sn);
        }
    }

    public String buildRequest(String orderType, String orderSn, Long paymentConfigId, Parameters parameters) throws PayException {
        return this.buildRequest(orderType, orderSn, paymentConfigId, "", parameters);
    }

    public String test(Long paymentConfigId, Parameters parameters) throws PayException {
        String orderType = "test";
//        List<Payment> payments = this.paymentDao.find(Restrictions.eq("paymentConfig.id", paymentConfigId), Restrictions.eq("orderType", orderType), Restrictions.eq("paymentStatus", Payment.PaymentStatus.ready));
//        String orderSn = payments.isEmpty() ? "TSN" + DateUtil.format("yyyyMMddHHmmss") : payments.get(0).getOrderSn();
//
//        Payment payment = this.ready(orderType, orderSn, "", paymentConfigId);
//        PaymentContext context = PaymentContext.newInstall(payment,this.orderServiceFactory.getOrderService(orderType));
//
//        // 支付参数
//        Map<String, String> parameterMap = context.getPayProduct().getParameterMap(parameters);
//        String sHtmlText = context.getPayProduct().buildRequest(parameterMap);
//        TagNode body = HtmlCleanerUtil.findFristTagNode(HtmlCleanerUtil.htmlCleaner(sHtmlText), "//body");
//        assert body != null;
//        body.removeChild(HtmlCleanerUtil.findFristTagNode(body, "//script"));
//        for (TagNode tagNode : HtmlCleanerUtil.findTagNodes(body, "//form//input")) {
//            if ("hidden".equals(tagNode.getAttributeByName("type"))) {
//                tagNode.addAttribute("type", "text");
//            } else if ("submit".equals(tagNode.getAttributeByName("type"))) {
//                tagNode.removeAttribute("style");
//            }
//        }
        return null;//HtmlCleanerUtil.getAsString(HtmlCleanerUtil.findFristTagNode(HtmlCleanerUtil.getAsString(body), "//form"));
    }

    /**
     * 提交支付请求
     *
     * @param orderType       订单类型
     * @param orderSn         订单编码
     * @param paymentConfigId 支付配置id
     * @param payMember       支付人
     * @param parameters      请求参数
     * @return html 表单字符串
     */
    public String buildRequest(String orderType, String orderSn, Long paymentConfigId, String payMember, Parameters parameters) throws PayException {
//        Payment payment = this.ready(orderType, orderSn, payMember, paymentConfigId);
//
//        PaymentContext context = this.createPaymentContext(payment.getSn());
//
//        PayProduct payProduct = context.getPayProduct();
//
//        // 支付参数
//        Map<String, String> parameterMap = payProduct.getParameterMap(parameters);
//        return payProduct.buildRequest(parameterMap);
        return "";
    }

    /*
    private void verify(Map<String, String> parameterMap) throws PayException {
//        Payment payment = PaymentContext.getContext().getPayment();
//        PayProduct payProduct = PaymentContext.getContext().getPayProduct();
//
//        PayResult payResult = payProduct.parsePayResult(parameterMap);
//        PaymentContext.getContext().setPayResult(payResult);
//
//        if (!payProduct.verifySign(parameterMap)) {
//            this.failure(payment.getSn());
//            throw new PayException("支付签名错误!");
//        } else if (PayResult.PayStatus.failure == payResult.getStatus()) {
//            this.failure(payment.getSn());
//            throw new PayException("支付失败!");
//        } else if (payment.getPaymentStatus() == Payment.PaymentStatus.success) {
//            LOG.debug("订单已支付");
//        }
    }*/

    /*
    public PaymentContext createPaymentContext(String sn) throws PayException {
        Payment payment = this.get(sn);
        if (payment == null) {
            throw new PayException("支付记录不存在!");
        }
        return null;//PaymentContext.newInstall(payment, this.orderServiceFactory.getOrderService(payment.getOrderType()));
    }*/

    /*
    public String payreturn(String sn, Map<String, String> parameterMap) throws PayException {
        PaymentContext context = createPaymentContext(sn);
        verify(parameterMap);
        this.success(sn, PaymentContext.getContext().getPayResult().getTradeNo());
        return null;//context.getPayProduct().getPayreturnMessage(sn);
    }*/

    /*
    public String paynotify(String sn, Map<String, String> parameterMap) throws PayException {
        PaymentContext context = createPaymentContext(sn);
        verify(parameterMap);
        this.success(sn, PaymentContext.getContext().getPayResult().getTradeNo());
        return null;//context.getPayProduct().getPaynotifyMessage(sn);
    }*/

}
