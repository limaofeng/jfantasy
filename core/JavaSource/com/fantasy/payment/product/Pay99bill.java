package com.fantasy.payment.product;

import com.fantasy.payment.bean.Payment;
import com.fantasy.payment.bean.PaymentConfig;
import com.fantasy.common.order.Order;
import com.fantasy.payment.service.PaymentContext;
import com.fantasy.system.util.SettingUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 快钱支付
 */

public class Pay99bill extends AbstractPaymentProduct {

    public static final String PAYMENT_URL = "https://www.99bill.com/gateway/recvMerchantInfoAction.htm";// 支付请求URL
    public static final String RETURN_URL = "/shop/payment!payreturn.action";// 回调处理URL

    @Override
    public String getPaymentUrl() {
        return PAYMENT_URL;
    }

    @Override
    public Map<String, String> getParameterMap(Parameters parameters) {
        PaymentContext context = PaymentContext.getContext();
        PaymentConfig paymentConfig = context.getPaymentConfig();
        Order orderDetails = context.getOrderDetails();
        Payment payment = context.getPayment();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String dateString = simpleDateFormat.format(new Date());
        String totalAmountString = String.format("%.2f", orderDetails.getPayableFee());

        String inputCharset = "1";// 字符集编码（1：UTF-8、2：GBK、3：GB2312）
        String bgUrl = SettingUtil.get("website", "ShopUrl") + RETURN_URL + "?paymentsn=" + payment.getSn();// 回调处理URL
        String version = "v2.0";// 网关版本
        String language = "1";// 显示语言种类（1：中文）
        String signType = "1";// 签名类型（1：MD5）
        String merchantAcctId = paymentConfig.getBargainorId();// 收款方账号
        AtomicReference<String> orderId = new AtomicReference<String>(payment.getSn());// 支付编号
        AtomicReference<String> orderAmount = new AtomicReference<String>(totalAmountString);// 总金额（单位：分）
        AtomicReference<String> orderTime = new AtomicReference<String>(dateString);// 订单提交时间
        AtomicReference<String> productName = new AtomicReference<String>(payment.getSn());// 商品名称
        AtomicReference<String> productDesc = new AtomicReference<String>(payment.getSn());// 商品描述
        String payType = "00";// 支付方式（00：显示所有支付方式、10：只显示银行卡支付方式、11：只显示电话银行支付方式、12：只显示快钱账户支付方式、13：只显示线下支付方式、14：只显示B2B支付方式）
        String redoFlag = "0";// 同一订单重复提交（1：禁止、0：允许）
        String key = paymentConfig.getBargainorKey();// 密钥

        // 生成签名
        Map<String, String> signMap = new LinkedHashMap<String, String>();
        signMap.put("inputCharset", inputCharset);
        signMap.put("bgUrl", bgUrl);
        signMap.put("version", version);
        signMap.put("language", language);
        signMap.put("signType", signType);
        signMap.put("merchantAcctId", merchantAcctId);
        signMap.put("orderId", orderId.get());
        signMap.put("orderAmount", orderAmount.get());
        signMap.put("orderTime", orderTime.get());
        signMap.put("productName", productName.get());
        signMap.put("productDesc", productDesc.get());
        signMap.put("payType", payType);
        signMap.put("redoFlag", redoFlag);
        signMap.put("key", key);
        String signMsg = DigestUtils.md5Hex(getParameterString(signMap)).toUpperCase();

        // 参数处理
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("inputCharset", inputCharset);
        parameterMap.put("bgUrl", bgUrl);
        parameterMap.put("version", version);
        parameterMap.put("language", language);
        parameterMap.put("signType", signType);
        parameterMap.put("merchantAcctId", merchantAcctId);
        parameterMap.put("orderId", orderId.get());
        parameterMap.put("orderAmount", orderAmount.get());
        parameterMap.put("orderTime", orderTime.get());
        parameterMap.put("productName", productName.get());
        parameterMap.put("productDesc", productDesc.get());
        parameterMap.put("payType", payType);
        parameterMap.put("redoFlag", redoFlag);
        parameterMap.put("signMsg", signMsg);

        return parameterMap;
    }

    @Override
    public boolean verifySign(Map<String, String> parameters) {
        PaymentConfig paymentConfig = PaymentContext.getContext().getPaymentConfig();
        // 获取参数
        String merchantAcctId = parameters.get("merchantAcctId");
        String version = parameters.get("version");
        String language = parameters.get("language");
        String signType = parameters.get("signType");
        String payType = parameters.get("payType");
        String bankId = parameters.get("bankId");
        String orderId = parameters.get("orderId");
        String orderTime = parameters.get("orderTime");
        String orderAmount = parameters.get("orderAmount");
        String dealId = parameters.get("dealId");
        String bankDealId = parameters.get("bankDealId");
        String dealTime = parameters.get("dealTime");
        String payAmount = parameters.get("payAmount");
        String fee = parameters.get("fee");
        String payResult = parameters.get("payResult");
        String errCode = parameters.get("errCode");
        String signMsg = parameters.get("signMsg");

        // 验证支付签名
        Map<String, String> signMap = new LinkedHashMap<String, String>();
        signMap.put("merchantAcctId", merchantAcctId);
        signMap.put("version", version);
        signMap.put("language", language);
        signMap.put("signType", signType);
        signMap.put("payType", payType);
        signMap.put("bankId", bankId);
        signMap.put("orderId", orderId);
        signMap.put("orderTime", orderTime);
        signMap.put("orderAmount", orderAmount);
        signMap.put("dealId", dealId);
        signMap.put("bankDealId", bankDealId);
        signMap.put("dealTime", dealTime);
        signMap.put("payAmount", payAmount);
        signMap.put("fee", fee);
        signMap.put("payResult", payResult);
        signMap.put("errCode", errCode);
        signMap.put("key", paymentConfig.getBargainorKey());
        return StringUtils.equals(signMsg, DigestUtils.md5Hex(getParameterString(signMap)).toUpperCase());
    }

    @Override
    public String getPaynotifyMessage(String paymentSn) {
        return null;
    }

    @Override
    public String getPayreturnMessage(String paymentSn) {
        return "<result>1</result><redirecturl>" + PaymentContext.getContext().getResultUrl(paymentSn) + "</redirecturl>";
    }

    @Override
    public PayResult parsePayResult(Map<String, String> parameters) {
        String payResult = parameters.get("payResult");
        StringUtils.equals(payResult, "10");
        return null;
    }

}