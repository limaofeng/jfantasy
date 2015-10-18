package com.fantasy.payment.service;

import com.fantasy.common.order.Order;
import com.fantasy.common.order.OrderService;
import com.fantasy.common.order.OrderServiceFactory;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.spring.mvc.error.NotFoundException;
import com.fantasy.framework.util.common.DateUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.htmlcleaner.HtmlCleanerUtil;
import com.fantasy.member.bean.Member;
import com.fantasy.member.service.MemberService;
import com.fantasy.payment.bean.Payment;
import com.fantasy.payment.bean.PaymentConfig;
import com.fantasy.payment.dao.PaymentDao;
import com.fantasy.payment.error.PaymentException;
import com.fantasy.payment.product.PayResult;
import com.fantasy.payment.product.PaymentProduct;
import com.fantasy.security.SpringSecurityUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Restrictions;
import org.htmlcleaner.TagNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


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
    private PaymentConfigService paymentConfigService;
    @Autowired(required = false)
    private PaymentConfiguration paymentConfiguration;
    @Autowired
    private MemberService memberService;
    @Autowired
    private OrderServiceFactory orderServiceFactory;

    /**
     * 支付准备
     *
     * @param orderType       订单类型
     * @param orderSn         订单编号
     * @param membername      会员
     * @param paymentConfigId 支付配置
     * @return Payment
     * @throws PaymentException
     */
    public Payment ready(String orderType, String orderSn, String membername, Long paymentConfigId) throws PaymentException {
        PaymentConfig paymentConfig = this.paymentConfigService.get(paymentConfigId);
        //在线支付
        if (PaymentConfig.PaymentConfigType.online != paymentConfig.getPaymentConfigType()) {
            throw new PaymentException("暂时只支持在线支付");
        }
        OrderService paymentOrderDetailsService = this.orderServiceFactory.getOrderService(orderType);
        //检查订单支付状态等信息
        Order orderDetails = paymentOrderDetailsService.loadOrderBySn(orderSn);

        //支付配置类型（线下支付、在线支付）
        Payment.PaymentType paymentType = Payment.PaymentType.online;
        BigDecimal paymentFee = BigDecimal.ZERO; //支付手续费

        BigDecimal amountPayable = orderDetails.getPayableFee();//应付金额（含支付手续费）

        Payment payment = this.paymentDao.findUnique(Restrictions.eq("paymentConfig.id", paymentConfigId), Restrictions.eq("orderType", orderType), Restrictions.eq("orderSn", orderSn), Restrictions.eq("paymentStatus", Payment.PaymentStatus.ready));
        if (payment != null) {
            //如果存在未完成的支付信息
            if (amountPayable.compareTo(payment.getTotalAmount().subtract(payment.getPaymentFee())) == 0) {
                return payment;
            } else {
                this.invalid(payment.getSn());
            }
        }

        payment = new Payment();

        //在线支付
        PaymentProduct paymentProduct = paymentConfiguration.getPaymentProduct(paymentConfig.getPaymentProductId());
        String bankName = paymentProduct.getName();
        String bankAccount = paymentConfig.getBargainorId();
        payment.setPaymentType(paymentType);
        payment.setPaymentConfigName(paymentConfig.getName());
        payment.setBankName(bankName);
        payment.setBankAccount(bankAccount);
        payment.setTotalAmount(amountPayable.add(paymentFee));
        payment.setPaymentFee(paymentFee);
        payment.setPayer(StringUtil.defaultValue(membername, SpringSecurityUtils.getCurrentUserName()));
        payment.setMemo(null);
        payment.setPaymentStatus(Payment.PaymentStatus.ready);
        payment.setPaymentConfig(paymentConfig);
        payment.setOrderType(orderType);
        payment.setOrderSn(orderSn);
        if (StringUtil.isNotBlank(membername)) {
            Member member = memberService.findUniqueByUsername(membername);
            if (member != null) {
                payment.setMember(member);
            }
        }
        return this.paymentDao.save(payment);
    }

    /**
     * 过期支付单
     *
     * @param sn 支付编号
     */
    public void invalid(String sn) {
        Payment payment = get(sn);
        payment.setPaymentStatus(Payment.PaymentStatus.invalid);
        this.paymentDao.save(payment);
    }

    public void close(String sn, String tradeNo) {
        Payment payment = get(sn);
        payment.setPaymentStatus(Payment.PaymentStatus.invalid);
        payment.setTradeNo(tradeNo);
        this.paymentDao.save(payment);
    }

    /**
     * 支付失败
     *
     * @param sn 支付编号
     */
    public void failure(String sn) {
        Payment payment = get(sn);
        payment.setPaymentStatus(Payment.PaymentStatus.failure);
        payment.setTradeNo(PaymentContext.getContext().getPayResult().getTradeNo());
        payment = this.paymentDao.save(payment);
        PaymentContext.getContext().payFailure(PaymentContext.getContext().getPayment());
    }

    public void failure(String sn, String tradeNo, String desc) {

    }

    /**
     * 付款成功
     *
     * @param sn 支付编号
     */
    public void success(String sn, String tradeNo) {
        Payment payment = get(sn);
        payment.setPaymentStatus(Payment.PaymentStatus.success);
        payment.setTradeNo(tradeNo);
        this.paymentDao.save(payment);
        //TODO 订单事件触发方式
        PaymentContext.getContext().paySuccess(PaymentContext.getContext().getPayment());
    }

    public Payment get(String sn) {
        return this.paymentDao.findUniqueBy("sn", sn);
    }

    public List<Payment> find(List<PropertyFilter> filters, String orderBy, String order) {
        return this.paymentDao.find(filters, orderBy, order);
    }

    public PaymentConfig getPaymentConfig(Long id) {
        return this.paymentConfigService.get(id);
    }

    public PaymentProduct getPaymentProduct(String paymentProductId) {
        return this.paymentConfiguration.getPaymentProduct(paymentProductId);
    }

    public void paynotify() {

    }

    public Pager<Payment> findPager(Pager<Payment> pager, List<PropertyFilter> filters) {
        return paymentDao.findPager(pager, filters);
    }

    public Payment get(Long id) {
        Payment payment = this.paymentDao.get(id);
        if (payment == null) {
            throw new NotFoundException("[id=" + id + "]对应的支付记录未找到");
        }
        return payment;
    }

    public void delete(Long... ids) {
        for (Long id : ids) {
            this.paymentDao.delete(id);
        }
    }

    public String buildRequest(String orderType, String orderSn, Long paymentConfigId, Map<String, String> parameters) throws PaymentException {
        return this.buildRequest(orderType, orderSn, paymentConfigId, "", parameters);
    }

    public String test(Long paymentConfigId, Map<String, String> parameters) throws PaymentException {
        String orderType = "test";
        List<Payment> payments = this.paymentDao.find(Restrictions.eq("paymentConfig.id", paymentConfigId), Restrictions.eq("orderType", orderType), Restrictions.eq("paymentStatus", Payment.PaymentStatus.ready));
        String orderSn = payments.isEmpty() ? "TSN" + DateUtil.format("yyyyMMddHHmmss") : payments.get(0).getOrderSn();
        PaymentContext context = PaymentContext.newInstall();

        Payment payment = this.ready(orderType, orderSn, "", paymentConfigId);
        context.setPayment(payment);

        PaymentProduct paymentProduct = this.getPaymentProduct(payment.getPaymentConfig().getPaymentProductId());
        OrderService paymentOrderDetailsService = this.orderServiceFactory.getOrderService(orderType);
        context.setPaymentProduct(paymentProduct);
        context.setOrderDetailsService(paymentOrderDetailsService);

        //检查订单支付状态等信息
        Order orderDetails = paymentOrderDetailsService.loadOrderBySn(orderSn);
        context.setOrderDetails(orderDetails);

        // 支付参数
        Map<String, String> parameterMap = paymentProduct.getParameterMap(parameters);
        String sHtmlText = paymentProduct.buildRequest(parameterMap);
        TagNode body = HtmlCleanerUtil.findFristTagNode(HtmlCleanerUtil.htmlCleaner(sHtmlText), "//body");
        assert body != null;
        body.removeChild(HtmlCleanerUtil.findFristTagNode(body, "//script"));
        for (TagNode tagNode : HtmlCleanerUtil.findTagNodes(body, "//form//input")) {
            if ("hidden".equals(tagNode.getAttributeByName("type"))) {
                tagNode.addAttribute("type", "text");
            } else if ("submit".equals(tagNode.getAttributeByName("type"))) {
                tagNode.removeAttribute("style");
            }
        }
        return HtmlCleanerUtil.getAsString(HtmlCleanerUtil.findFristTagNode(HtmlCleanerUtil.getAsString(body), "//form"));
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
    public String buildRequest(String orderType, String orderSn, Long paymentConfigId, String payMember, Map<String, String> parameters) throws PaymentException {
        Payment payment = this.ready(orderType, orderSn, payMember, paymentConfigId);

        PaymentContext context = this.createPaymentContext(payment.getSn());

        PaymentProduct paymentProduct = context.getPaymentProduct();

        // 支付参数
        Map<String, String> parameterMap = paymentProduct.getParameterMap(parameters);
        return paymentProduct.buildRequest(parameterMap);
    }

    private void verify(Map<String, String> parameterMap) throws PaymentException {
        Payment payment = PaymentContext.getContext().getPayment();
        PaymentProduct paymentProduct = PaymentContext.getContext().getPaymentProduct();

        PayResult payResult = paymentProduct.parsePayResult(parameterMap);
        PaymentContext.getContext().setPayResult(payResult);

        if (!paymentProduct.verifySign(parameterMap)) {
            this.failure(payment.getSn());
            throw new PaymentException("支付签名错误!");
        } else if (PayResult.PayStatus.failure == payResult.getStatus()) {
            this.failure(payment.getSn());
            throw new PaymentException("支付失败!");
        } else if (payment.getPaymentStatus() == Payment.PaymentStatus.success) {
            LOG.debug("订单已支付");
        }
    }

    public PaymentContext createPaymentContext(String sn) throws PaymentException {
        PaymentContext context = PaymentContext.newInstall();
        Payment payment = this.get(sn);
        if (payment == null) {
            throw new PaymentException("支付记录不存在!");
        }
        context.setPayment(payment);

        PaymentProduct paymentProduct = this.getPaymentProduct(payment.getPaymentConfig().getPaymentProductId());
        if (paymentProduct == null) {
            throw new PaymentException("支付产品不存在!");
        }
        context.setPaymentProduct(paymentProduct);

        OrderService paymentOrderDetailsService = this.orderServiceFactory.getOrderService(payment.getOrderType());
        context.setOrderDetailsService(paymentOrderDetailsService);

        context.initOrderUrls();

        Order orderDetails = paymentOrderDetailsService.loadOrderBySn(payment.getOrderSn());
        context.setOrderDetails(orderDetails);
        return context;
    }

    public String payreturn(String sn, Map<String, String> parameterMap) throws PaymentException {
        PaymentContext context = createPaymentContext(sn);
        verify(parameterMap);
        this.success(sn, PaymentContext.getContext().getPayResult().getTradeNo());
        return context.getPaymentProduct().getPayreturnMessage(sn);
    }

    public String paynotify(String sn, Map<String, String> parameterMap) throws PaymentException {
        PaymentContext context = createPaymentContext(sn);
        verify(parameterMap);
        this.success(sn, PaymentContext.getContext().getPayResult().getTradeNo());
        return context.getPaymentProduct().getPaynotifyMessage(sn);
    }

    public Order getOrderByPaymentId(Long id) {
        Payment payment = this.get(id);
        OrderService orderService = orderServiceFactory.getOrderService(payment.getOrderType());
        return orderService.loadOrderBySn(payment.getOrderSn());
    }

}
