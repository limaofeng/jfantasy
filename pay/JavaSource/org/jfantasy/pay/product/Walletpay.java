package org.jfantasy.pay.product;

import com.fasterxml.jackson.databind.JsonNode;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.spring.SpringContextUtil;
import org.jfantasy.framework.spring.mvc.error.RestException;
import org.jfantasy.pay.bean.*;
import org.jfantasy.pay.bean.enums.TxStatus;
import org.jfantasy.pay.error.PayException;
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
        //获取支付账户 与 支付密码
        String password = properties.getProperty(PROPERTY_PASSWORD);
        String trx_no = properties.getProperty(PROPERTY_TRANSACTION);

        payService().notify(payment, JSON.serialize(properties));

        //等待处理结果。

        //返回处理结果
        return null;
    }

    @Override
    public Object app(Payment payment, Order order, Properties properties) throws PayException {
        return super.app(payment, order, properties);
    }

    @Override
    public Object payNotify(Payment payment, String result) throws PayException {
        JsonNode jsonNode = JSON.deserialize(result);
        assert jsonNode != null;
        String trx_no = jsonNode.get("trx_no").asText();
        String password = jsonNode.get("password").asText();
        accountService().remit(trx_no,password);

        Transaction transaction = payService.loadTransaction(trx_no);
        //修改状态,自动触发划账操作
        transaction.setStatus(TxStatus.processing);
        //返回支付结果
        return payment;
    }


    @Override
    public String refund(Refund refund) {
        payService().notify(refund, "");
        return "";
    }

    @Override
    public Object payNotify(Refund refund, String result) throws PayException {
        return super.payNotify(refund, result);
    }

}
