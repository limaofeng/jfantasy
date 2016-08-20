package org.jfantasy.pay.product;

import org.apache.commons.lang.StringUtils;
import org.jfantasy.framework.error.IgnoreException;
import org.jfantasy.pay.bean.Order;
import org.jfantasy.pay.bean.PayConfig;
import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.bean.Refund;
import org.jfantasy.pay.error.PayException;
import org.jfantasy.pay.order.entity.enums.PaymentStatus;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 易宝支付
 */

public class Yeepay extends PayProductSupport {

    public static final String PAYMENT_URL = "https://www.yeepay.com/app-merchant-proxy/node";// 支付请求URL
    public static final String RETURN_URL = "/shop/payment!payreturn.action";// 回调处理URL

    public String getPaymentUrl() {
        return PAYMENT_URL;
    }

    public Map<String, String> getParameterMap(Parameters parameters) {
        PayConfig paymentConfig = null;//context.getPaymentConfig();
        Payment payment = null;//context.getPayment();
        BigDecimal paymentAmount = payment.getTotalAmount();
        String paymentSn = payment.getSn();

        String p0Cmd = "Buy";// 业务类型
        String p1MerId = paymentConfig.getBargainorId();// 商户编号
        //String p2_Order = paymentSn;// 支付编号
        String p3Amt = paymentAmount.toString();// 总金额（单位：元）
        String p4Cur = "CNY";// 支付币种（CNY：人民币）
        String p5Pid = paymentSn + ":商品名称";// 商品名称
        String p6Pcat = "";// 商品种类
        String p7Pdesc = paymentSn + ":商品描述";// 商品描述
        String p8Url = SettingUtil.getServerUrl() + RETURN_URL + "?paymentsn=" + paymentSn;// 回调处理URL
        String p9SAF = "0";// 是否需要填写送货地址（1：是、0：否）
        String paMP = "shop" + "xx";// 商户数据
        String pdFrpId = "";// 支付通道编码
        String prNeedResponse = "1";// 是否需要应答机制（1：是、0：否）
        String key = paymentConfig.getBargainorKey();// 密钥

        // 生成签名
        /*p2_Order == paymentSn */
        String hmac = hmacSign(p0Cmd + p1MerId + paymentSn + p3Amt + p4Cur + p5Pid + p6Pcat + p7Pdesc + p8Url + p9SAF + paMP + pdFrpId + prNeedResponse, key);

        // 参数处理
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("p0_Cmd", p0Cmd);
        parameterMap.put("p1_MerId", p1MerId);
        parameterMap.put("p2_Order", paymentSn);
        parameterMap.put("p3_Amt", p3Amt);
        parameterMap.put("p4_Cur", p4Cur);
        parameterMap.put("p5_Pid", p5Pid);
        parameterMap.put("p6_Pcat", p6Pcat);
        parameterMap.put("p7_Pdesc", p7Pdesc);
        parameterMap.put("p8_Url", p8Url);
        parameterMap.put("p9_SAF", p9SAF);
        parameterMap.put("pa_MP", paMP);
        parameterMap.put("pd_FrpId", pdFrpId);
        parameterMap.put("pr_NeedResponse", prNeedResponse);
        parameterMap.put("hmac", hmac);

        return parameterMap;
    }

    public boolean verifySign(Map<String, String> parameters) {
        PayConfig paymentConfig = null;//PaymentContext.getContext().getPaymentConfig();
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
            throw new IgnoreException(e.getMessage(),e);
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
            throw new IgnoreException(e.getMessage(),e);
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