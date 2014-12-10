package com.fantasy.payment.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.common.DateUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.htmlcleaner.HtmlCleanerUtil;
import com.fantasy.member.bean.Member;
import com.fantasy.member.service.MemberService;
import com.fantasy.member.userdetails.MemberUser;
import com.fantasy.payment.bean.Payment;
import com.fantasy.payment.bean.PaymentConfig;
import com.fantasy.payment.dao.PaymentDao;
import com.fantasy.payment.error.PaymentException;
import com.fantasy.payment.product.PaymentProduct;
import com.fantasy.security.SpringSecurityUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Restrictions;
import org.htmlcleaner.TagNode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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

    @Resource
    private PaymentDao paymentDao;
    @Resource
    private PaymentConfigService paymentConfigService;
    @Resource
    private PaymentConfiguration paymentConfiguration;
    @Resource
    private MemberService memberService;

    public Payment ready(String orderType, String orderSn, String membername, Long paymentConfigId) {
        PaymentConfig paymentConfig = this.paymentConfigService.get(paymentConfigId);
        PaymentOrderDetailsService paymentOrderDetailsService = this.paymentConfiguration.getPaymentOrderService(orderType);
        //检查订单支付状态等信息
        OrderDetails orderDetails = paymentOrderDetailsService.loadOrderBySn(orderSn);

        Payment payment = new Payment();
        //支付配置类型（线下支付、在线支付）
        PaymentConfig.PaymentConfigType paymentConfigType = paymentConfig.getPaymentConfigType();
        Payment.PaymentType paymentType = Payment.PaymentType.online;
        BigDecimal paymentFee = BigDecimal.ZERO; //支付手续费

        BigDecimal amountPayable = orderDetails.getPayableFee();//应付金额（含支付手续费）

        Payment oldPayment = this.paymentDao.findUnique(Restrictions.eq("orderType", orderType), Restrictions.eq("orderSn", orderSn), Restrictions.eq("paymentStatus", Payment.PaymentStatus.ready));
        if (oldPayment != null) {
            //如果存在未完成的支付信息
            if (amountPayable.equals(oldPayment.getTotalAmount().subtract(oldPayment.getPaymentFee()))) {
                return oldPayment;
            } else {
                this.invalid(oldPayment.getSn());
            }
        }

        //在线支付
        if (PaymentConfig.PaymentConfigType.online == paymentConfigType) {
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
                    payment.setMember(SpringSecurityUtils.getCurrentUser(MemberUser.class).getUser());
                }
            }
            this.paymentDao.save(payment);
        }
        return payment;
    }

    public void invalid(String sn) {
        Payment payment = get(sn);
        payment.setPaymentStatus(Payment.PaymentStatus.invalid);
        this.paymentDao.save(payment);
    }

    public void failure(String sn) {
        Payment payment = get(sn);
        payment.setPaymentStatus(Payment.PaymentStatus.failure);
        this.paymentDao.save(payment);
    }

    /**
     * 付款成功
     *
     * @param sn 支付编号
     */
    public void success(String sn) {
        Payment payment = get(sn);
        payment.setPaymentStatus(Payment.PaymentStatus.success);
        this.paymentDao.save(payment);
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
        return this.paymentDao.get(id);
    }

    public void delete(Long... ids) {
        for (Long id : ids) {
            this.paymentDao.delete(id);
        }
    }

    public String submit(String orderType, String orderSn, Long paymentConfigId, Map<String, String> parameters) {
        return this.submit(orderType, orderSn, paymentConfigId, "", parameters);
    }

    public String test(Long paymentConfigId, Map<String, String> parameters) {
        String orderType = "test";
        String orderSn = "TSN" + DateUtil.format("yyyyMMddHHmmss");
        PaymentContext context = PaymentContext.newInstall();

        Payment payment = this.ready(orderType, orderSn, "", paymentConfigId);
        context.setPayment(payment);

        PaymentProduct paymentProduct = this.getPaymentProduct(payment.getPaymentConfig().getPaymentProductId());
        PaymentOrderDetailsService paymentOrderDetailsService = this.paymentConfiguration.getPaymentOrderService(orderType);

        //检查订单支付状态等信息
        OrderDetails orderDetails = paymentOrderDetailsService.loadOrderBySn(orderSn);
        context.setOrderDetails(orderDetails);

        // 支付参数
        Map<String, String> parameterMap = paymentProduct.getParameterMap(parameters);
        String sHtmlText = paymentProduct.buildRequest(parameterMap);
        TagNode body = HtmlCleanerUtil.findFristTagNode(HtmlCleanerUtil.htmlCleaner(sHtmlText),"//body");
        body.removeChild(HtmlCleanerUtil.findFristTagNode(body,"//script"));
        for(TagNode tagNode : HtmlCleanerUtil.findTagNodes(body,"//form//input")){
            if("hidden".equals(tagNode.getAttributeByName("type"))){
                tagNode.addAttribute("type","text");
            }else if("submit".equals(tagNode.getAttributeByName("type"))){
                tagNode.removeAttribute("style");
            }
        }
        return HtmlCleanerUtil.getAsString(HtmlCleanerUtil.findFristTagNode(HtmlCleanerUtil.getAsString(body),"//form"));
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
    public String submit(String orderType, String orderSn, Long paymentConfigId, String payMember, Map<String, String> parameters) {
        PaymentContext context = PaymentContext.newInstall();

        Payment payment = this.ready(orderType, orderSn, payMember, paymentConfigId);
        context.setPayment(payment);

        PaymentProduct paymentProduct = this.getPaymentProduct(payment.getPaymentConfig().getPaymentProductId());
        PaymentOrderDetailsService paymentOrderDetailsService = this.paymentConfiguration.getPaymentOrderService(orderType);

        //检查订单支付状态等信息
        OrderDetails orderDetails = paymentOrderDetailsService.loadOrderBySn(orderSn);
        context.setOrderDetails(orderDetails);

        // 支付参数
        Map<String, String> parameterMap = paymentProduct.getParameterMap(parameters);
        return paymentProduct.buildRequest(parameterMap);
    }

    private void verify(String sn, Map<String, String> parameterMap) throws PaymentException {
        Payment payment = PaymentContext.getContext().getPayment();
        PaymentProduct paymentProduct = PaymentContext.getContext().getPaymentProduct();

        boolean isSuccess = paymentProduct.isPaySuccess(parameterMap);

        if (!paymentProduct.verifySign(parameterMap)) {
            this.failure(sn);
            throw new PaymentException("支付签名错误!");
        } else if (!isSuccess) {
            this.failure(sn);
            throw new PaymentException("支付失败!");
        } else if (payment.getPaymentStatus() == Payment.PaymentStatus.success) {
            LOG.debug("订单已支付");
            //throw new PaymentException("订单已支付!");
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

        PaymentOrderDetailsService paymentOrderDetailsService = this.paymentConfiguration.getPaymentOrderService(payment.getOrderType());
        OrderDetails orderDetails = paymentOrderDetailsService.loadOrderBySn(payment.getOrderSn());
        context.setOrderDetails(orderDetails);
        return context;
    }

    public String payreturn(String sn, Map<String, String> parameterMap) throws PaymentException {
        PaymentContext context = createPaymentContext(sn);
        verify(sn, parameterMap);
        this.success(sn);
        return context.getPaymentProduct().getPayreturnMessage(sn);
    }

    public String paynotify(String sn, Map<String, String> parameterMap) throws PaymentException {
        PaymentContext context = createPaymentContext(sn);
        verify(sn, parameterMap);
        this.success(sn);
        return context.getPaymentProduct().getPaynotifyMessage(sn);
    }

}
