package com.fantasy.payment.product;

import com.fantasy.payment.bean.Payment;
import com.fantasy.payment.bean.PaymentConfig;
import com.fantasy.payment.service.PaymentContext;
import com.fantasy.system.util.SettingUtil;
import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 易宝支付
 */

public class Yeepay extends AbstractPaymentProduct {

    public static final String PAYMENT_URL = "https://www.yeepay.com/app-merchant-proxy/node";// 支付请求URL
    public static final String RETURN_URL = "/shop/payment!payreturn.action";// 回调处理URL

    // 支持货币种类
    // public static final CurrencyType[] currencyType = {CurrencyType.CNY};

    @Override
    public String getPaymentUrl() {
        return PAYMENT_URL;
    }

    @Override
    public Map<String, String> getParameterMap(Map<String, String> parameters) {
        PaymentContext context = PaymentContext.getContext();
        PaymentConfig paymentConfig = context.getPaymentConfig();
        Payment payment = context.getPayment();
        BigDecimal paymentAmount = payment.getTotalAmount();
        String paymentSn = payment.getSn();

        String p0_Cmd = "Buy";// 业务类型
        String p1_MerId = paymentConfig.getBargainorId();// 商户编号
        //String p2_Order = paymentSn;// 支付编号
        String p3_Amt = paymentAmount.toString();// 总金额（单位：元）
        String p4_Cur = "CNY";// 支付币种（CNY：人民币）
        String p5_Pid = paymentSn + ":商品名称";// 商品名称
        String p6_Pcat = "";// 商品种类
        String p7_Pdesc = paymentSn + ":商品描述";// 商品描述
        String p8_Url = SettingUtil.get("website", "ShopUrl") + RETURN_URL + "?paymentsn=" + paymentSn;// 回调处理URL
        String p9_SAF = "0";// 是否需要填写送货地址（1：是、0：否）
        String pa_MP = "shop" + "xx";// 商户数据
        String pd_FrpId = "";// 支付通道编码
        String pr_NeedResponse = "1";// 是否需要应答机制（1：是、0：否）
        String key = paymentConfig.getBargainorKey();// 密钥

        // 生成签名
        /*p2_Order == paymentSn */
        String hmac = hmacSign(p0_Cmd + p1_MerId + paymentSn + p3_Amt + p4_Cur + p5_Pid + p6_Pcat + p7_Pdesc + p8_Url + p9_SAF + pa_MP + pd_FrpId + pr_NeedResponse, key);

        // 参数处理
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("p0_Cmd", p0_Cmd);
        parameterMap.put("p1_MerId", p1_MerId);
        parameterMap.put("p2_Order", paymentSn);
        parameterMap.put("p3_Amt", p3_Amt);
        parameterMap.put("p4_Cur", p4_Cur);
        parameterMap.put("p5_Pid", p5_Pid);
        parameterMap.put("p6_Pcat", p6_Pcat);
        parameterMap.put("p7_Pdesc", p7_Pdesc);
        parameterMap.put("p8_Url", p8_Url);
        parameterMap.put("p9_SAF", p9_SAF);
        parameterMap.put("pa_MP", pa_MP);
        parameterMap.put("pd_FrpId", pd_FrpId);
        parameterMap.put("pr_NeedResponse", pr_NeedResponse);
        parameterMap.put("hmac", hmac);

        return parameterMap;
    }

    @Override
    public boolean verifySign(Map<String, String> parameters) {
        PaymentConfig paymentConfig = PaymentContext.getContext().getPaymentConfig();
        // 获取参数
        String p1MerId = parameters.get("p1_MerId");
        String r0Cmd = parameters.get("r0_Cmd");
        String r1Code = parameters.get("r1_Code");
        String r2TrxId = parameters.get("r2_TrxId");
        String r3Amt = parameters.get("r3_Amt");
        String r4Cur = parameters.get("r4_Cur");
        String r5Pid = parameters.get("r5_Pid");
        String r6Order = parameters.get("r6_Order");
        String r7Uid = parameters.get("r7_Uid");
        String r8Mp = parameters.get("r8_MP");
        String r9BType = parameters.get("r9_BType");
        String hmac = parameters.get("hmac");

        // 验证支付签名
        return StringUtils.equals(hmac, hmacSign(p1MerId + r0Cmd + r1Code + r2TrxId + r3Amt + r4Cur + r5Pid + r6Order + r7Uid + r8Mp + r9BType, paymentConfig.getBargainorKey()));
    }

    // HMAC加密算法
    private String hmacSign(String value, String key) {
        String encodingCharset = "UTF-8";
        byte kIpad[] = new byte[64];
        byte kOpad[] = new byte[64];
        byte keys[];
        byte values[];
        try {
            keys = key.getBytes(encodingCharset);
            values = value.getBytes(encodingCharset);
        } catch (UnsupportedEncodingException e) {
            keys = key.getBytes();
            values = value.getBytes();
        }

        Arrays.fill(kIpad, keys.length, 64, (byte) 54);
        Arrays.fill(kOpad, keys.length, 64, (byte) 92);
        for (int i = 0; i < keys.length; i++) {
            kIpad[i] = (byte) (keys[i] ^ 0x36);
            kOpad[i] = (byte) (keys[i] ^ 0x5c);
        }

        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        messageDigest.update(kIpad);
        messageDigest.update(values);
        byte digest[] = messageDigest.digest();
        messageDigest.reset();
        messageDigest.update(kOpad);
        messageDigest.update(digest, 0, 16);
        digest = messageDigest.digest();
        if (digest == null) {
            return null;
        }
        StringBuilder stringBuffer = new StringBuilder(digest.length * 2);
        for (byte aDigest : digest) {
            int current = aDigest & 0xff;
            if (current < 16){
                stringBuffer.append("0");
            }
            stringBuffer.append(Integer.toString(current, 16));
        }
        return stringBuffer.toString();
    }

    @Override
    public PayResult parsePayResult(Map<String, String> parameters) {
        //getPaymentSn
        //parameters.get("r6_Order")
        //getPaymentAmount
        //parameters.get("r3_Amt")
        PayResult payResult = new PayResult();
        payResult.setTradeNo(parameters.get("r6_Order"));//交易流水号
        payResult.setTotalFee(BigDecimal.valueOf(Double.valueOf(parameters.get("r3_Amt"))));//交易金额
        String r1Code = parameters.get("r1_Code");
        payResult.setStatus(StringUtils.equals(r1Code, "1")?PayResult.PayStatus.success:PayResult.PayStatus.failure);
        return payResult;
    }

}