package com.fantasy.payment.product;

import com.fantasy.payment.bean.Payment;
import com.fantasy.payment.bean.PaymentConfig;
import com.fantasy.payment.service.PaymentContext;
import com.fantasy.system.util.SettingUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 财付通（即时交易）
 */
public class TenpayDirect extends AbstractPaymentProduct {

    public static final String PAYMENT_URL = "http://service.tenpay.com/cgi-bin/v3.0/payservice.cgi";// 支付请求URL
    public static final String RETURN_URL = "/shop/payment!payreturn.action";// 回调处理URL

    // 支持货币种类
    public static final CurrencyType[] currencyType = {CurrencyType.CNY};

    @Override
    public String getPaymentUrl() {
        return PAYMENT_URL;
    }

    public boolean isPaySuccess(Map<String, String> parameters) {
        if (parameters == null) {
            return false;
        }
        /*
        parameters.get("sp_billno")
        new BigDecimal(parameters.get("total_fee")).divide(new BigDecimal(100))
        */
        String payResult = parameters.get("pay_result");
        return StringUtils.equals(payResult, "0");
    }

    @Override
    public Map<String, String> getParameterMap(Map<String, String> parameters) {
        PaymentContext context = PaymentContext.getContext();
        PaymentConfig paymentConfig = context.getPaymentConfig();
        Payment payment = context.getPayment();
        BigDecimal paymentAmount = payment.getTotalAmount();
        String paymentSn = payment.getSn();

        String transactionId = buildTenpayTransactionId(paymentConfig.getBargainorId(), paymentSn);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String dateString = simpleDateFormat.format(new Date());
        String totalAmountString = paymentAmount.multiply(new BigDecimal(100)).setScale(0).toString();

        String cmdno = "1";// 业务代码（1：即时交易支付）
        String date = dateString;// 订单提交时间
        String bank_type = "0";// 银行类型（0：财付通）
        String desc = paymentSn;// 订单描述
        String purchaser_id = "";// 客户财付通帐户
        String bargainor_id = paymentConfig.getBargainorId();// 商户号
        String transaction_id = transactionId;// 交易号
        String sp_billno = paymentSn;// 支付编号
        String total_fee = totalAmountString;// 总金额（单位：分）
        String fee_type = "1";// 支付币种（1：人民币）
        String return_url = SettingUtil.get("website", "ShopUrl") + RETURN_URL + "?paymentsn=" + paymentSn;// 回调处理URL
        String attach = "s" + "h" + "o" + "p" + "x" + "x";// 商户数据
        String spbill_create_ip = parameters.get("remoteAddr");// 客户IP
        String key = paymentConfig.getBargainorKey();// 密钥

        // 生成签名
        Map<String, String> signMap = new LinkedHashMap<String, String>();
        signMap.put("cmdno", cmdno);
        signMap.put("date", date);
        signMap.put("bargainor_id", bargainor_id);
        signMap.put("transaction_id", transaction_id);
        signMap.put("sp_billno", sp_billno);
        signMap.put("total_fee", total_fee);
        signMap.put("fee_type", fee_type);
        signMap.put("return_url", return_url);
        signMap.put("attach", attach);
        signMap.put("spbill_create_ip", spbill_create_ip);
        signMap.put("key", key);
        String sign = DigestUtils.md5Hex(getParameterString(signMap)).toUpperCase();

        // 参数处理
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("cmdno", cmdno);
        parameterMap.put("date", date);
        parameterMap.put("bargainor_id", bargainor_id);
        parameterMap.put("transaction_id", transaction_id);
        parameterMap.put("sp_billno", sp_billno);
        parameterMap.put("total_fee", total_fee);
        parameterMap.put("fee_type", fee_type);
        parameterMap.put("return_url", return_url);
        parameterMap.put("attach", attach);
        parameterMap.put("spbill_create_ip", spbill_create_ip);
        parameterMap.put("bank_type", bank_type);
        parameterMap.put("desc", desc);
        parameterMap.put("purchaser_id", purchaser_id);
        parameterMap.put("sign", sign);
        parameterMap.put("cs", "utf-8");

        return parameterMap;
    }

    @Override
    public boolean verifySign(Map<String, String> parameters) {
        PaymentConfig paymentConfig = PaymentContext.getContext().getPaymentConfig();
        // 财付通（即时交易）
        String cmdno = parameters.get("cmdno");
        String pay_result = parameters.get("pay_result");
        String date = parameters.get("date");
        String transaction_id = parameters.get("transaction_id");
        String sp_billno = parameters.get("sp_billno");
        String total_fee = parameters.get("total_fee");
        String fee_type = parameters.get("fee_type");
        String attach = parameters.get("attach");
        String sign = parameters.get("sign");

        // 验证支付签名
        Map<String, String> parameterMap = new LinkedHashMap<String, String>();
        parameterMap.put("cmdno", cmdno);
        parameterMap.put("pay_result", pay_result);
        parameterMap.put("date", date);
        parameterMap.put("transaction_id", transaction_id);
        parameterMap.put("sp_billno", sp_billno);
        parameterMap.put("total_fee", total_fee);
        parameterMap.put("fee_type", fee_type);
        parameterMap.put("attach", attach);
        parameterMap.put("key", paymentConfig.getBargainorKey());
        return StringUtils.equals(sign, DigestUtils.md5Hex(getParameterString(parameterMap)).toUpperCase());
    }

    @Override
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

}