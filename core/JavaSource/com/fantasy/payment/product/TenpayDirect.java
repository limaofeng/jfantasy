package com.fantasy.payment.product;

import com.fantasy.payment.bean.PaymentConfig;
import com.fantasy.system.util.SettingUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
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

    @Override
    public String getPaymentSn(HttpServletRequest httpServletRequest) {
        if (httpServletRequest == null) {
            return null;
        }
        String spBillno = httpServletRequest.getParameter("sp_billno");
        if (StringUtils.isEmpty(spBillno)) {
            return null;
        }
        return spBillno;
    }

    @Override
    public BigDecimal getPaymentAmount(HttpServletRequest httpServletRequest) {
        if (httpServletRequest == null) {
            return null;
        }
        String totalFee = httpServletRequest.getParameter("total_fee");
        if (StringUtils.isEmpty(totalFee)) {
            return null;
        }
        return new BigDecimal(totalFee).divide(new BigDecimal(100));
    }

    public boolean isPaySuccess(HttpServletRequest httpServletRequest) {
        if (httpServletRequest == null) {
            return false;
        }
        String payResult = httpServletRequest.getParameter("pay_result");
        if (StringUtils.equals(payResult, "0")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Map<String, String> getParameterMap(PaymentConfig paymentConfig, String paymentSn, BigDecimal paymentAmount, HttpServletRequest httpServletRequest) {
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
        String spbill_create_ip = httpServletRequest.getRemoteAddr();// 客户IP
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
    public boolean verifySign(PaymentConfig paymentConfig, HttpServletRequest httpServletRequest) {
        // 财付通（即时交易）
        String cmdno = httpServletRequest.getParameter("cmdno");
        String pay_result = httpServletRequest.getParameter("pay_result");
        String date = httpServletRequest.getParameter("date");
        String transaction_id = httpServletRequest.getParameter("transaction_id");
        String sp_billno = httpServletRequest.getParameter("sp_billno");
        String total_fee = httpServletRequest.getParameter("total_fee");
        String fee_type = httpServletRequest.getParameter("fee_type");
        String attach = httpServletRequest.getParameter("attach");
        String sign = httpServletRequest.getParameter("sign");

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
        if (StringUtils.equals(sign, DigestUtils.md5Hex(getParameterString(parameterMap)).toUpperCase())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getPayreturnMessage(String paymentSn) {
        return "<html><head><meta name=\"TENCENT_ONLINE_PAYMENT\" content=\"China TENCENT\"><meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" /><title>页面跳转中..</title></head><body onload=\"javascript: document.forms[0].submit();\"><form action=\"" + SettingUtil.get("website", "ShopUrl") + RESULT_URL + "\"><input type=\"hidden\" name=\"paymentsn\" value=\"" + paymentSn + "\" /></form></body></html>";
    }

    @Override
    public String getPaynotifyMessage() {
        return null;
    }

    // 根据商户号、支付编号生成财付通交易号
    private String buildTenpayTransactionId(String bargainorId, String paymentSn) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String dateString = simpleDateFormat.format(new Date());
        int count = 10 - paymentSn.length();
        if (count > 0) {
            StringBuffer stringBuffer = new StringBuffer();
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