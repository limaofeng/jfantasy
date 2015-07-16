package com.fantasy.payment.product;

import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.web.WebUtil;
import com.fantasy.payment.bean.Payment;
import com.fantasy.payment.bean.PaymentConfig;
import com.fantasy.payment.order.OrderDetails;
import com.fantasy.payment.service.PaymentContext;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 支付宝（即时交易）
 */
public class AlipayDirect extends AbstractAlipayPaymentProduct {

    public static final String PAYMENT_URL = "https://mapi.alipay.com/gateway.do?_input_charset=" + input_charset;// 支付请求URL

    /*
    public static final String RETURN_URL = "/payment/payreturn.do";// 回调处理URL
    public static final String NOTIFY_URL = "/payment/paynotify.do";// 消息通知URL
    public static final String SHOW_URL = "/payment.do";// 支付单回显url
    */

    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.00");

    public static final Map<String, String> CREDIT_BANKCODES = new LinkedHashMap<String, String>();
    public static final Map<String, String> DEBIT_BANKCODES = new LinkedHashMap<String, String>();

    static {
        //银行简码——混合渠道
        CREDIT_BANKCODES.put("ICBCBTB", "中国工商银行（B2B）");
        CREDIT_BANKCODES.put("ABCBTB", "中国农业银行（B2B）");
        CREDIT_BANKCODES.put("CCBBTB", "中国建设银行（B2B）");
        CREDIT_BANKCODES.put("SPDBB2B", "上海浦东发展银行（B2B）");
        CREDIT_BANKCODES.put("BOCBTB", "中国银行（B2B）");
        CREDIT_BANKCODES.put("CMBBTB", "招商银行（B2B）");
        CREDIT_BANKCODES.put("BOCB2C", "中国银行");
        CREDIT_BANKCODES.put("ICBCB2C", "中国工商银行");
        CREDIT_BANKCODES.put("CMB", "招商银行");
        CREDIT_BANKCODES.put("CCB", "中国建设银行");
        CREDIT_BANKCODES.put("ABC", "中国农业银行");
        CREDIT_BANKCODES.put("SPDB", "上海浦东发展银行");
        CREDIT_BANKCODES.put("CIB", "兴业银行");
        CREDIT_BANKCODES.put("GDB", "广发银行");
        CREDIT_BANKCODES.put("CMBC", "中国民生银行");
        CREDIT_BANKCODES.put("CITIC", "中信银行");
        CREDIT_BANKCODES.put("HZCBB2C", "杭州银行");
        CREDIT_BANKCODES.put("CEBBANK", "中国光大银行");
        CREDIT_BANKCODES.put("SHBANK", "上海银行");
        CREDIT_BANKCODES.put("NBBANK", "宁波银行");
        CREDIT_BANKCODES.put("SPABANK", "平安银行");
        CREDIT_BANKCODES.put("BJRCB", "北京农村商业银行");
        CREDIT_BANKCODES.put("FDB", "富滇银行");
        CREDIT_BANKCODES.put("POSTGC", "中国邮政储蓄银行");
        CREDIT_BANKCODES.put("abc1003", "visa");
        CREDIT_BANKCODES.put(" abc1004", "master");
        //银行简码——纯借记卡渠道
        DEBIT_BANKCODES.put("CMB-DEBIT", "招商银行");
        DEBIT_BANKCODES.put("CCB-DEBIT", "中国建设银行");
        DEBIT_BANKCODES.put("ICBC-DEBIT", "中国工商银行");
        DEBIT_BANKCODES.put("COMM-DEBIT", "交通银行");
        DEBIT_BANKCODES.put("GDB-DEBIT", "广发银行");
        DEBIT_BANKCODES.put("BOC-DEBIT", "中国银行");
        DEBIT_BANKCODES.put("CEB-DEBIT", "中国光大银行");
        DEBIT_BANKCODES.put("SPDB-DEBIT", "上海浦东发展银行");
        DEBIT_BANKCODES.put("PSBC-DEBIT", "中国邮政储蓄银行");
        DEBIT_BANKCODES.put("BJBANK", "北京银行");
        DEBIT_BANKCODES.put("SHRCB", "上海农商银行");
        DEBIT_BANKCODES.put("WZCBB2C-DEBIT", "温州银行");
        DEBIT_BANKCODES.put("COMM", "交通银行");
    }

    @Override
    public String getPaymentUrl() {
        return PAYMENT_URL;
    }

    @Override
    public Map<String, String> getParameterMap(Map<String, String> parameters) {
        PaymentContext context = PaymentContext.getContext();
        PaymentConfig paymentConfig = context.getPaymentConfig();
        OrderDetails orderDetails = context.getOrderDetails();
        Payment payment = context.getPayment();

        AtomicReference<String> body = new AtomicReference<String>(orderDetails.getSN());// 订单描述
        String defaultbank = parameters.get("bankNo");// 默认选择银行（当paymethod为bankPay时有效）
        String extra_common_param = "";// 商户数据
        String notify_url = context.getNotifyUrl(payment.getSn());// 消息通知URL
        AtomicReference<String> out_trade_no = new AtomicReference<String>(payment.getSn());// 支付编号
        String partner = paymentConfig.getBargainorId();// 合作身份者ID
        String payment_type = "1";// 支付类型（固定值：1）
        String paymethod = StringUtil.isBlank(defaultbank) ? "directPay" : "bankPay";// 默认支付方式（bankPay：网银、cartoon：卡通、directPay：余额、CASH：网点支付）
        String return_url = context.getReturnUrl(payment.getSn());// 回调处理URL
        String seller_id = paymentConfig.getSellerEmail();// 商家ID
        String service = "create_direct_pay_by_user";// 接口类型（create_direct_pay_by_user：即时交易）
        String show_url = context.getShowUrl(payment.getOrderSn());// 商品显示URL
        String sign_type = "MD5";//签名加密方式（MD5）
        AtomicReference<String> subject = new AtomicReference<String>(orderDetails.getSubject());// 订单的名称、标题、关键字等
        String total_fee = DECIMAL_FORMAT.format(orderDetails.getPayableFee());// 总金额（单位：元）
        String key = paymentConfig.getBargainorKey();// 密钥
        //防钓鱼时间戳
        String anti_phishing_key = "";
        //若要使用请调用类文件submit中的query_timestamp函数
        //客户端的IP地址
        String exter_invoke_ip = "";
        //非局域网的外网IP地址，如：221.0.0.1

        // 生成签名
        Map<String, String> signMap = new LinkedHashMap<String, String>();
        signMap.put("service", service);
        signMap.put("partner", partner);
        signMap.put("_input_charset", input_charset);
        signMap.put("payment_type", payment_type);
        signMap.put("notify_url", notify_url);
        signMap.put("return_url", return_url);
        signMap.put("seller_email", seller_id);
        signMap.put("out_trade_no", out_trade_no.get());
        signMap.put("subject", subject.get());
        signMap.put("total_fee", total_fee);
        signMap.put("body", body.get());
        signMap.put("show_url", show_url);
        signMap.put("anti_phishing_key", anti_phishing_key);
        signMap.put("exter_invoke_ip", exter_invoke_ip);
        signMap.put("paymethod", paymethod);
        signMap.put("defaultbank", defaultbank);
        signMap.put("extra_common_param", extra_common_param);

        String sign = DigestUtils.md5Hex(getParameterString(signMap) + key);

        // 参数处理
        Map<String, String> parameterMap = new HashMap<String, String>(paraFilter(signMap));
        parameterMap.put("sign_type", sign_type);
        parameterMap.put("sign", sign);
        return parameterMap;
    }

    @Override
    public boolean verifySign(Map<String, String> parameters) {
        PaymentContext context = PaymentContext.getContext();
        PaymentConfig paymentConfig = context.getPaymentConfig();

        Map<String, String> params = new HashMap<String, String>();
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            params.put(entry.getKey(), WebUtil.transformCoding(entry.getValue(), "ISO-8859-1", "utf-8"));
        }

        //移除回调链接中的 paymentSn sign不是true，与安全校验码、请求时的参数格式（如：带自定义参数等）、编码格式有关
        params.remove("sn");
        return StringUtils.equals(params.get("sign"), DigestUtils.md5Hex(getParameterString(paraFilter(params)) + paymentConfig.getBargainorKey())) && verifyResponse(paymentConfig.getBargainorId(), params.get("notify_id"));
    }

    @Override
    public PayResult parsePayResult(Map<String, String> parameters) {
        Map<String, String> params = new HashMap<String, String>();
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            params.put(entry.getKey(), WebUtil.transformCoding(entry.getValue(), "ISO-8859-1", "utf-8"));
        }
        PayResult payResult = new PayResult();
        payResult.setPaymentSN(params.get("out_trade_no"));//支付编号
        payResult.setTradeNo(params.get("trade_no"));//交易流水号
        payResult.setTotalFee(BigDecimal.valueOf(Double.valueOf(params.get("total_fee"))));//交易金额
        String tradeStatus = params.get("trade_status");
        payResult.setStatus((StringUtils.equals(tradeStatus, "TRADE_FINISHED") || StringUtils.equals(tradeStatus, "TRADE_SUCCESS")) ? PayResult.PayStatus.success : PayResult.PayStatus.failure);
        return payResult;
    }

    public static Map<String, String> getDebitBankcodes() {
        return AlipayDirect.DEBIT_BANKCODES;
    }

    public static Map<String, String> getCreditBankcodes() {
        return AlipayDirect.CREDIT_BANKCODES;
    }

}