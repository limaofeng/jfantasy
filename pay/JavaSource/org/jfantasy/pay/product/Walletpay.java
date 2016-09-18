package org.jfantasy.pay.product;

import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.spring.SpringContextUtil;
import org.jfantasy.pay.bean.Order;
import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.bean.Refund;
import org.jfantasy.pay.error.PayException;
import org.jfantasy.pay.order.entity.enums.PaymentStatus;
import org.jfantasy.pay.service.AccountService;
import org.jfantasy.pay.service.PayService;

import java.util.Properties;

/**
 * 钱包支付
 */
public class Walletpay extends PayProductSupport {

    private static String PROPERTY_ACCOUNT = "account";
    private static String PROPERTY_PASSWORD = "password";
    private static String PROPERTY_TRANSACTION = "transaction";

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
        return this.transaction(payment,order,properties);
    }

    public Object transaction(Payment payment, Order order, Properties properties) {
        //获取支付账户 与 支付密码
        String password = properties.getProperty(PROPERTY_PASSWORD);
        String trx_no = properties.getProperty(PROPERTY_TRANSACTION);
        //进行划账操作
        this.accountService().remit(trx_no, password);
        //触发通知
        return this.payService().paymentNotify(payment.getSn(), JSON.serialize(properties));
    }

    @Override
    public Object app(Payment payment, Order order, Properties properties) throws PayException {
        return this.transaction(payment, order, properties);
    }

    @Override
    public Object payNotify(Payment payment, String result) throws PayException {
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
