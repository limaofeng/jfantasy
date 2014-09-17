package com.fantasy.payment.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.dao.mybatis.keygen.util.SequenceInfo;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.member.userdetails.MemberUser;
import com.fantasy.payment.bean.Payment;
import com.fantasy.payment.bean.PaymentConfig;
import com.fantasy.payment.dao.PaymentDao;
import com.fantasy.payment.error.PaymentException;
import com.fantasy.payment.product.PaymentProduct;
import com.fantasy.security.SpringSecurityUtils;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;


/**
 * 支付service
 */
@Service
@Transactional
public class PaymentService {

    @Resource
    private PaymentDao paymentDao;
    @Resource
    private PaymentConfigService paymentConfigService;
    @Resource
    private PaymentConfiguration paymentConfiguration;

    /**
     * 准备支付
     *
     * @param payment 准备支付
     */
    public void ready(Payment payment) {
        this.paymentDao.save(payment);
    }

    public Payment ready(String orderType, String orderSn, Long paymentConfigId) throws PaymentException {
        PaymentConfig paymentConfig = this.paymentConfigService.get(paymentConfigId);
        PaymentOrderService paymentOrderService = this.paymentConfiguration.getPaymentOrderService(orderType);
        //检查订单支付状态等信息
        paymentOrderService.payCheck(orderSn);

        Payment payment = new Payment();
        //支付配置类型（线下支付、在线支付）
        PaymentConfig.PaymentConfigType paymentConfigType = paymentConfig.getPaymentConfigType();
        Payment.PaymentType paymentType = Payment.PaymentType.online;
        BigDecimal paymentFee = BigDecimal.ZERO; //支付手续费

        BigDecimal amountPayable = paymentOrderService.amountPayable(orderSn);//应付金额（含支付手续费）

        Payment oldPayment = this.paymentDao.findUnique(Restrictions.eq("orderType", orderType), Restrictions.eq("orderSn", orderSn), Restrictions.eq("paymentStatus", Payment.PaymentStatus.ready));
        if (oldPayment != null) {//如果存在未完成的支付信息
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
            payment.setPayer(SpringSecurityUtils.getCurrentUserName());
            payment.setMemo(null);
            payment.setPaymentStatus(Payment.PaymentStatus.ready);
            payment.setPaymentConfig(paymentConfig);
            payment.setOrderType(orderType);
            payment.setOrderSn(orderSn);
            if (SpringSecurityUtils.getCurrentUser() instanceof MemberUser) {
                payment.setMember(SpringSecurityUtils.getCurrentUser(MemberUser.class).getUser());
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

    public void failure(String sn){
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

    public Payment test(BigDecimal amount, Long paymentConfigId) {
        PaymentConfig paymentConfig = this.paymentConfigService.get(paymentConfigId);
        PaymentProduct paymentProduct = this.paymentConfiguration.getPaymentProduct(paymentConfig.getPaymentProductId());
        Payment payment = new Payment();
        String bankName = paymentProduct.getName();
        String bankAccount = paymentConfig.getBargainorId();
        payment.setPaymentType(Payment.PaymentType.online);
        payment.setPaymentConfigName(paymentConfig.getName());
        payment.setBankName(bankName);
        payment.setBankAccount(bankAccount);
        payment.setTotalAmount(amount);
        payment.setPaymentFee(BigDecimal.ZERO);
        payment.setPayer(SpringSecurityUtils.getCurrentUserName());
        payment.setMemo("支付测试");
        payment.setPaymentStatus(Payment.PaymentStatus.ready);
        payment.setMember(null);
        payment.setPaymentConfig(paymentConfig);
        payment.setOrderType("test");
        payment.setOrderSn(payment.getOrderType() + "_" + StringUtil.addZeroLeft(String.valueOf(SequenceInfo.nextValue(payment.getOrderType())), 6));
        return payment;
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

    public void paynotify(){

    }


    public Pager<Payment> findPager(Pager<Payment> pager, List<PropertyFilter> filters) {
        return paymentDao.findPager(pager, filters);
    }

    public Payment get(Long id){
        return this.paymentDao.get(id);
    }

    public void delete(Long... ids){
        for(Long id : ids){
            this.paymentDao.delete(id);
        }
    }

}
