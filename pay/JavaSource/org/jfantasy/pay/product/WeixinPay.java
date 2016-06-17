package org.jfantasy.pay.product;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.jfantasy.framework.httpclient.HttpClientUtil;
import org.jfantasy.framework.httpclient.Request;
import org.jfantasy.framework.httpclient.Response;
import org.jfantasy.framework.spring.mvc.error.RestException;
import org.jfantasy.framework.util.common.DateUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.sax.XMLReader;
import org.jfantasy.framework.util.sax.XmlElement;
import org.jfantasy.framework.util.web.WebUtil;
import org.jfantasy.pay.bean.Order;
import org.jfantasy.pay.bean.PayConfig;
import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.bean.Refund;
import org.jfantasy.pay.error.PayException;
import org.jfantasy.pay.order.entity.enums.PaymentStatus;
import org.jfantasy.pay.order.entity.enums.RefundStatus;
import org.jfantasy.pay.product.sign.SignUtil;
import org.jfantasy.pay.product.util.CertUtil;
import org.jfantasy.pay.product.util.RAMFileProxy;
import org.springframework.util.DigestUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

/**
 * 订单 err_code 一览:
 * ORDERPAID	订单已支付	订单已支付，不能发起关单	订单已支付，不能发起关单，请当作已支付的正常交易
 * SYSTEMERROR	系统错误	系统错误	系统异常，请重新调用该API
 * ORDERNOTEXIST	订单不存在	订单系统不存在此订单	不需要关单，当作未提交的支付的订单
 * ORDERCLOSED	订单已关闭	订单已关闭，无法重复关闭	订单已关闭，无需继续调用
 * SIGNERROR	签名错误	参数签名结果不正确	请检查签名参数和方法是否都符合签名算法要求
 * REQUIRE_POST_METHOD	请使用post方法	未使用post传递参数 	请检查请求参数是否通过post方法提交
 * XML_FORMAT_ERROR	XML格式错误	XML格式错误	请检查XML参数格式是否正确
 */
public class Weixinpay extends PayProductSupport {

    private static Urls urls = new Urls();

    static {
        urls.setUnifiedorderUrl("https://api.mch.weixin.qq.com/pay/unifiedorder");
        urls.setCloseorderUrl("https://api.mch.weixin.qq.com/pay/closeorder");
        urls.setRefundUrl("https://api.mch.weixin.qq.com/secapi/pay/refund");
    }

    @Override
    public Object web(Payment payment, Order order, Properties properties) throws PayException {
        PayConfig config = payment.getPayConfig();
        String openid = properties.getProperty("openid");

        Map<String, String> data = unifiedorder(payment, order, properties);

        if ("FAIL".equals(data.get("result_code"))) {
            // ORDERPAID == data.get("err_code") 订单已支付
            Map<String, String> result = new HashMap<>();
            result.put("return_code", data.get("return_code"));
            result.put("result_code", data.get("result_code"));
            result.put("return_msg", data.get("return_msg"));
            result.put("err_code_des", data.get("err_code_des"));
            result.put("nonce_str", generateNonceString(32));
            result.put("sign", sign(result, config.getBargainorKey()));
            return mapToXml(result);
        }
        if ("NATIVE".equals(data.get("trade_type"))) {//扫码支付
            Map<String, String> result = new HashMap<>();
            result.put("return_code", data.get("return_code"));
            result.put("result_code", data.get("result_code"));
            result.put("appid", data.get("appid"));
            result.put("mch_id", data.get("mch_id"));
            result.put("nonce_str", generateNonceString(32));
            result.put("prepay_id", data.get("prepay_id"));
            result.put("sign", sign(result, config.getBargainorKey()));
            return StringUtil.isNotBlank(openid) ? mapToXml(result) : data.get("code_url");
        } else if ("JSAPI".equals(data.get("trade_type"))) {
            Map<String, String> result = new HashMap<>();
            result.put("appId", data.get("appid"));
            result.put("timeStamp", (DateUtil.now().getTime() / 1000) + "");
            result.put("nonceStr", generateNonceString(32));
            result.put("package", "prepay_id=" + data.get("prepay_id"));
            result.put("signType", "MD5");
            result.put("paySign", sign(result, config.getBargainorKey()));
            return result;
        }
        return null;
    }

    @Override
    public Map<String, String> app(Payment payment, Order order, Properties properties) throws PayException {
        PayConfig config = payment.getPayConfig();

        properties.put("trade_type", "APP");
        Map<String, String> data = unifiedorder(payment, order, properties);
        if ("APP".equals(data.get("trade_type"))) {//APP支付
            Map<String, String> result = new TreeMap<>();
            result.put("appid", config.get("appid", String.class));
            result.put("partnerid", config.getBargainorId());
            result.put("prepayid", data.get("prepay_id"));
            result.put("package", "Sign=WXPay");
            result.put("noncestr", generateNonceString(32));
            result.put("timestamp", (DateUtil.now().getTime() / 1000) + "");
            result.put("sign", sign(result, config.getBargainorKey()));
            return result;
        }
        throw new PayException(" trade_type 错误");
    }

    @Override
    public String payNotify(Payment payment, String result) throws PayException {
        PayConfig config = payment.getPayConfig();
        try {
            Map<String, String> data = xmlToMap(result);

            if (!"SUCCESS".equals(data.get("return_code"))) {
                throw new RestException(data.get("return_msg"));
            }
            //验证签名
            if (!verify(data, config.getBargainorKey())) {
                throw new RestException("微信返回的响应签名错误");
            }
            payment.setTradeNo(data.get("transaction_id"));
            payment.setTradeTime(DateUtil.now());
            payment.setStatus("SUCCESS".equals(data.get("result_code")) ? PaymentStatus.success : PaymentStatus.failure);

            data.clear();
            data.put("return_code", "SUCCESS");
            data.put("return_msg", "OK");
            data.put("sign", sign(data, config.getBargainorKey()));
            return mapToXml(data);
        } finally {
            this.log("in", "notify", payment, config, result);
        }
    }

    @Override
    public String refund(Refund refund) {
        PayConfig config = refund.getPayConfig();
        Payment payment = refund.getPayment();
        try {
            //组装数据
            Map<String, String> data = new TreeMap<>();
            data.put("appid", config.get("appid", String.class));
            data.put("mch_id", config.getBargainorId());
            data.put("nonce_str", generateNonceString(16));
            data.put("transaction_id", payment.getTradeNo());
            data.put("out_refund_no", refund.getSn());
            data.put("total_fee", payment.getTotalAmount().multiply(BigDecimal.valueOf(100d)).intValue() + "");
            data.put("refund_fee", refund.getTotalAmount().multiply(BigDecimal.valueOf(100d)).intValue() + "");
            data.put("refund_fee_type", "CNY");//货币类型
            data.put("op_user_id", config.getBargainorId());
            data.put("sign", sign(data, config.getBargainorKey()));

            //指定读取证书格式为PKCS12 
            KeyStore keyStore = CertUtil.loadKeyStore(new RAMFileProxy(config, "encryptCert"), config.getBargainorId());
            Request request = new Request(new StringEntity(mapToXml(data), ContentType.TEXT_XML));
            request.enabledSSL(keyStore, config.getBargainorId());

            Response response = HttpClientUtil.doPost(urls.getRefundUrl(), request);

            //解析数据
            data = xmlToMap(response.getBody());
            //判断业务处理是否成功
            if (!"SUCCESS".equalsIgnoreCase(data.get("return_code"))) {
                throw new RestException(data.get("return_msg"));
            }

            //验证签名
            if (!verify(data, config.getBargainorKey())) {
                throw new RestException("微信返回的响应签名错误");
            }

            if ("FAIL".equals(data.get("result_code"))) {
                throw new RestException("[" + data.get("err_code") + "]" + data.get("err_code_des"));
            }

            refund.setStatus(RefundStatus.wait);
            refund.setTradeNo(data.get("refund_id"));

            //TODO 微信退款是否成功必须调用退款查询接口, 提交退款申请后，通过调用该接口查询退款状态。退款有一定延时，用零钱支付的退款20分钟内到账，银行卡支付的退款3个工作日后重新查询退款状态。
            return null;
        } catch (IOException e) {
            throw new RestException("调用微信接口,网络错误!");
        } catch (PayException e) {
            throw new RestException(e.getMessage());
        }
    }

    @Override
    public String payNotify(Refund refund, String result) throws PayException {
        throw new RestException("微信不支持退款通知接口");
    }

    /**
     * 微信支付 - 统一下单接口
     */
    private Map<String, String> unifiedorder(Payment payment, Order order, Properties prop) {
        PayConfig config = payment.getPayConfig();
        //获取订单信息
        try {
            //组装数据
            Map<String, String> data = new TreeMap<>();
            data.put("appid", config.get("appid", String.class));
            data.put("mch_id", config.getBargainorId());
            data.put("device_info", "WEB");
            data.put("body", order.getSubject());
            data.put("detail", order.getBody());
            data.put("nonce_str", generateNonceString(16));
            String openid = prop.getProperty("openid");
            if (StringUtil.isNotBlank(openid)) {
                data.put("openid", openid);
            }
            data.put("trade_type", prop.getProperty("trade_type", "JSAPI"));
            String productid = prop.getProperty("product_id");
            if (StringUtil.isNotBlank(productid)) {
                data.put("product_id", productid);
            }
            data.put("notify_url", SettingUtil.getServerUrl() + "/pays/" + payment.getSn() + "/notify");
            data.put("out_trade_no", payment.getSn());
            String[] serverIps = WebUtil.getServerIps();
            data.put("spbill_create_ip", serverIps.length == 0 ? "127.0.0.1" : serverIps[0]);
            data.put("total_fee", payment.getTotalAmount().multiply(BigDecimal.valueOf(100d)).intValue() + "");
            data.put("sign", sign(data, config.getBargainorKey()));

            Response response = HttpClientUtil.doPost(urls.getUnifiedorderUrl(), new Request(new StringEntity(WebUtil.transformCoding(mapToXml(data), "utf-8", "ISO8859-1"), ContentType.TEXT_XML)));

            //解析数据
            data = xmlToMap(response.getBody());

            //判断业务处理是否成功
            if (!"SUCCESS".equalsIgnoreCase(data.get("return_code"))) {
                throw new RestException(data.get("return_msg"));
            }

            //验证签名
            if (!verify(data, config.getBargainorKey())) {
                throw new RestException("微信返回的响应签名错误");
            }

            if (StringUtil.isNotBlank(data.get("err_code"))) {
                throw new RestException("[" + data.get("err_code") + "]" + data.get("err_code_des"));
            }

            return data;
        } catch (IOException e) {
            throw new RestException("调用微信接口,网络错误!");
        } catch (PayException e) {
            throw new RestException(e.getMessage());
        }
    }

    /**
     * 订单支付查询
     *
     * @param payment 支付记录
     * @return PrePayment
     */
    public Payment query(Payment payment) {
        try {
            PayConfig config = payment.getPayConfig();
            PayConfig paymentConfig = payment.getPayConfig();
            //组装数据
            Map<String, String> data = new TreeMap<>();
            data.put("appid", config.get("appid", String.class));
            data.put("mch_id", paymentConfig.getBargainorId());
            data.put("out_trade_no", payment.getSn());
            data.put("nonce_str", generateNonceString(16));
            data.put("sign", sign(data, config.getBargainorKey()));

            Response response = HttpClientUtil.doPost("https://api.mch.weixin.qq.com/pay/orderquery", new Request(new StringEntity(WebUtil.transformCoding(mapToXml(data), "utf-8", "ISO8859-1"), ContentType.TEXT_XML)));
            LOG.debug("微信端响应:" + response.getBody());
            //解析数据
            data = xmlToMap(response.getBody());

            //判断业务处理是否成功
            if (!"SUCCESS".equalsIgnoreCase(data.get("result_code"))) {
                throw new RestException(data.get("return_msg"));
            }

            //验证签名
            if (!verify(data, paymentConfig.getBargainorKey())) {
                throw new RestException("微信返回的响应签名错误");
            }
            String tradeNo = data.get("transaction_id");
            String state = data.get("trade_state");
            if ("SUCCESS".equalsIgnoreCase(state)) {//支付成功
                payment.setStatus(PaymentStatus.success);
            } else if ("CLOSED".equalsIgnoreCase(state)) {//已关闭
                payment.setStatus(PaymentStatus.close);
            } else if ("PAYERROR".equalsIgnoreCase(state) || "REFUND".equalsIgnoreCase(state) || "REVOKED".equalsIgnoreCase(state)) {
                //支付失败 and 转入退款 and 已撤销
                payment.setStatus(PaymentStatus.failure);
            }
            return payment;
        } catch (IOException e) {
            throw new RestException("调用微信接口,网络错误!");
        } catch (PayException e) {
            throw new RestException(e.getMessage());
        }
    }

    /**
     * 关闭订单 - 原交易单超时或者支付超时,需要关闭原支付单
     *
     * @return boolean
     */
    public boolean closeorder(Payment payment) {
        PayConfig config = payment.getPayConfig();
        try {
            //组装数据
            Map<String, String> data = new TreeMap<>();
            data.put("appid", config.get("appid", String.class));
            data.put("mch_id", config.getBargainorId());
            data.put("nonce_str", generateNonceString(16));
            data.put("out_trade_no", payment.getSn());
            data.put("sign", sign(data, config.getBargainorKey()));

            Response response = HttpClientUtil.doPost(urls.getCloseorderUrl(), new Request(new StringEntity(mapToXml(data), ContentType.TEXT_XML)));
            //解析数据
            data = xmlToMap(response.getBody());
            //判断业务处理是否成功
            if (!"SUCCESS".equalsIgnoreCase(data.get("return_code"))) {
                throw new RestException(data.get("return_msg"));
            }

            //验证签名
            if (!verify(data, config.getBargainorKey())) {
                throw new RestException("微信返回的响应签名错误");
            }

            if ("FAIL".equals(data.get("result_code"))) {
                return false;
            }

            payment.setStatus(PaymentStatus.close);

            return true;
        } catch (IOException e) {
            throw new RestException("调用微信接口,网络错误!");
        } catch (PayException e) {
            throw new RestException(e.getMessage());
        }
    }

    private static Map<String, String> xmlToMap(String xml) {
        Map<String, String> data = new TreeMap<>();
        //解析数据
        XmlElement xmlElement = XMLReader.reader(new ByteArrayInputStream(xml.getBytes()));
        assert xmlElement != null;
        data.clear();
        for (XmlElement node : xmlElement.getChildNodes()) {
            if (StringUtil.isBlank(node.getContent())) {
                continue;
            }
            data.put(node.getTagName(), node.getContent());
        }
        return data;
    }

    private static String mapToXml(Map<String, String> data) {
        StringBuilder xml = new StringBuilder("<xml>");
        for (Map.Entry<String, String> entry : data.entrySet()) {
            xml.append("<").append(entry.getKey()).append(">").append(entry.getValue()).append("</").append(entry.getKey()).append(">");
        }
        xml.append("</xml>");
        return xml.toString();
    }

    public static boolean verify(Map<String, String> data, String bargainorKey) {
        return data.get("sign").equals(Weixinpay.sign(data, bargainorKey));
    }

    public static String sign(Map<String, String> data, String bargainorKey) {
        return DigestUtils.md5DigestAsHex((SignUtil.coverMapString(data, "sign") + "&key=" + bargainorKey).getBytes()).toUpperCase();
    }

    private static final String NONCE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private static String generateNonceString(int length) {
        int maxPos = NONCE_CHARS.length();
        String noceStr = "";
        for (int i = 0; i < length; i++) {
            noceStr += NONCE_CHARS.charAt((int) Math.floor(Math.random() * maxPos));
        }
        return noceStr;
    }

    private static class Urls {

        String unifiedorderUrl;
        String closeorderUrl;
        String refundUrl;

        String getUnifiedorderUrl() {
            return unifiedorderUrl;
        }

        void setUnifiedorderUrl(String url) {
            this.unifiedorderUrl = url;
        }

        void setCloseorderUrl(String url) {
            this.closeorderUrl = url;
        }

        String getCloseorderUrl() {
            return closeorderUrl;
        }

        String getRefundUrl() {
            return refundUrl;
        }

        void setRefundUrl(String refundUrl) {
            this.refundUrl = refundUrl;
        }
    }

}