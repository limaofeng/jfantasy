package org.jfantasy.pay.product;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.jfantasy.pay.bean.Order;
import org.jfantasy.pay.bean.PayConfig;
import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.bean.Refund;
import org.jfantasy.pay.error.PayException;
import org.jfantasy.pay.order.entity.enums.PaymentStatus;
import org.jfantasy.pay.product.sign.SignUtil;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 财付通（即时交易）
 */
public class TenpayDirect extends PayProductSupport {

    public static final String PAYMENT_URL = "http://service.tenpay.com/cgi-bin/v3.0/payservice.cgi";// 支付请求URL
    public static final String RETURN_URL = "/shop/payment!payreturn.action";// 回调处理URL

    // 支持货币种类
    public static final CurrencyType[] currencyType = {CurrencyType.CNY};

    public String getPaymentUrl() {
        return PAYMENT_URL;
    }

    public Map<String, String> getParameterMap(Parameters parameters) {
//        PaymentContext context = PaymentContext.getContext();
        PayConfig paymentConfig = null;//context.getPaymentConfig();
        Payment payment = null;//context.getPayment();
        BigDecimal paymentAmount = payment.getTotalAmount();
        String paymentSn = payment.getSn();

        String transactionId = buildTenpayTransactionId(paymentConfig.getBargainorId(), paymentSn);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String dateString = simpleDateFormat.format(new Date());
        String totalAmountString = paymentAmount.multiply(new BigDecimal(100)).setScale(0).toString();

        String cmdno = "1";// 业务代码（1：即时交易支付）
        String date = dateString;// 订单提交时间
        String bankType = "0";// 银行类型（0：财付通）
        String desc = paymentSn;// 订单描述
        String purchaserId = "";// 客户财付通帐户
        String bargainorId = paymentConfig.getBargainorId();// 商户号
        String transactionIds = transactionId;// 交易号
        String spBillno = paymentSn;// 支付编号
        String totalFee = totalAmountString;// 总金额（单位：分）
        String feeType = "1";// 支付币种（1：人民币）
        String returnUrl = SettingUtil.getServerUrl() + RETURN_URL + "?paymentsn=" + paymentSn;// 回调处理URL
        String attach = "s" + "h" + "o" + "p" + "x" + "x";// 商户数据
        String spbillCreateIp = parameters.get("remoteAddr");// 客户IP
        String key = paymentConfig.getBargainorKey();// 密钥

        // 生成签名
        Map<String, String> signMap = new LinkedHashMap<String, String>();
        signMap.put("cmdno", cmdno);
        signMap.put("date", date);
        signMap.put("bargainor_id", bargainorId);
        signMap.put("transaction_id", transactionIds);
        signMap.put("sp_billno", spBillno);
        signMap.put("total_fee", totalFee);
        signMap.put("fee_type", feeType);
        signMap.put("return_url", returnUrl);
        signMap.put("attach", attach);
        signMap.put("spbill_create_ip", spbillCreateIp);
        signMap.put("key", key);
        String sign = DigestUtils.md5Hex(SignUtil.coverMapString(signMap)).toUpperCase();

        // 参数处理
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("cmdno", cmdno);
        parameterMap.put("date", date);
        parameterMap.put("bargainor_id", bargainorId);
        parameterMap.put("transaction_id", transactionIds);
        parameterMap.put("sp_billno", spBillno);
        parameterMap.put("total_fee", totalFee);
        parameterMap.put("fee_type", feeType);
        parameterMap.put("return_url", returnUrl);
        parameterMap.put("attach", attach);
        parameterMap.put("spbill_create_ip", spbillCreateIp);
        parameterMap.put("bank_type", bankType);
        parameterMap.put("desc", desc);
        parameterMap.put("purchaser_id", purchaserId);
        parameterMap.put("sign", sign);
        parameterMap.put("cs", "utf-8");

        return parameterMap;
    }

    public boolean verifySign(Map<String, String> parameters) {
        PayConfig paymentConfig = null;//PaymentContext.getContext().getPaymentConfig();
        // 财付通（即时交易）
        String cmdno = parameters.get("cmdno");
        String payResult = parameters.get("pay_result");
        String date = parameters.get("date");
        String transactionId = parameters.get("transaction_id");
        String spBillno = parameters.get("sp_billno");
        String totalFee = parameters.get("total_fee");
        String feeType = parameters.get("fee_type");
        String attach = parameters.get("attach");
        String sign = parameters.get("sign");

        // 验证支付签名
        Map<String, String> parameterMap = new LinkedHashMap<String, String>();
        parameterMap.put("cmdno", cmdno);
        parameterMap.put("pay_result", payResult);
        parameterMap.put("date", date);
        parameterMap.put("transaction_id", transactionId);
        parameterMap.put("sp_billno", spBillno);
        parameterMap.put("total_fee", totalFee);
        parameterMap.put("fee_type", feeType);
        parameterMap.put("attach", attach);
        parameterMap.put("key", paymentConfig.getBargainorKey());
        return StringUtils.equals(sign, DigestUtils.md5Hex(SignUtil.coverMapString(parameterMap)).toUpperCase());
    }

    public String getPaynotifyMessage(String paymentSn) {
        return null;
    }

    // 根据商户号、支付编号生成财付通交易号
    private String buildTenpayTransactionId(String bargainorId, String paymentSn) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String dateString = simpleDateFormat.format(new Date());
        int count = 10 - paymentSn.length();
        if (count > 0) {
            StringBuilder stringBuffer = new StringBuilder();
            for (int i = 0; i < count; i++) {
                stringBuffer.append("0");
            }
            stringBuffer.append(paymentSn);
            paymentSn = stringBuffer.toString();
        } else {
            paymentSn = StringUtils.substring(paymentSn, count);
        }
        return bargainorId + dateString + paymentSn;
    }

    public PayResult parsePayResult(Map<String, String> parameters) {
                /*
        parameters.get("sp_billno")
        new BigDecimal(parameters.get("total_fee")).divide(new BigDecimal(100))
        */
        String payResult = parameters.get("pay_result");
        StringUtils.equals(payResult, "0");
        return null;
    }

    @Override
    public String web(Payment payment, Order order, Properties properties) {
        return null;
    }

    @Override
    public Payment payNotify(Payment payment,  String result) throws PayException{
        return null;
    }

    @Override
    public Refund payNotify(Refund refund, String result) throws PayException {
        return null;
    }

    @Override
    public PaymentStatus query(Payment payment) throws PayException {
        return null;
    }

    @Override
    public void close(Payment payment) throws PayException {

    }

}