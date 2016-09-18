package org.jfantasy.pay.product;

import org.jfantasy.framework.spring.SpringContextUtil;
import org.jfantasy.framework.util.common.DateUtil;
import org.jfantasy.pay.bean.Order;
import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.bean.Refund;
import org.jfantasy.pay.bean.Transaction;
import org.jfantasy.pay.error.PayException;
import org.jfantasy.pay.order.entity.enums.PaymentStatus;
import org.jfantasy.pay.service.AccountService;
import org.jfantasy.pay.service.PayService;

import java.util.Properties;

/**
 * 钱包支付
 */
public class Walletpay extends PayProductSupport {

    private AccountService accountService;

    private AccountService accountService() {
        if (accountService == null) {
            return accountService = SpringContextUtil.getBeanByType(AccountService.class);
        }
        return accountService;
    }

    private PayService payService;

    private PayService payService() {
        if (payService == null) {
            return payService = SpringContextUtil.getBeanByType(PayService.class);
        }
        return payService;
    }

    @Override
    public Object web(Payment payment, Order order, Properties properties) throws PayException {
        return this.transaction(payment, order, properties);
    }

    public Object transaction(Payment payment, Order order, Properties properties) {
        //获取支付账户 与 支付密码
        String PROPERTY_PASSWORD = "password";
        String password = properties.getProperty(PROPERTY_PASSWORD);
        Transaction transaction = (Transaction) properties.get(PROPERTY_TRANSACTION);
        //进行划账操作
        this.accountService().remit(transaction.getSn(), password);
        //触发通知
        return this.payService().paymentNotify(payment.getSn(), "");
    }

    @Override
    public Object app(Payment payment, Order order, Properties properties) throws PayException {
        return this.transaction(payment, order, properties);
    }

    @Override
    public Object payNotify(Payment payment, String result) throws PayException {
        payment.setTradeNo(payment.getTransaction().getSn());
        payment.setTradeTime(DateUtil.now());
        payment.setStatus(PaymentStatus.success);
        return "success";
    }

    @Override
    public String refund(Refund refund) {
        return "";
    }

    @Override
    public PaymentStatus query(Payment payment) throws PayException {
        return null;
    }

    @Override
    public void close(Payment payment) throws PayException {

    }

    @Override
    public Object payNotify(Refund refund, String result) throws PayException {
        return super.payNotify(refund, result);
    }

}
