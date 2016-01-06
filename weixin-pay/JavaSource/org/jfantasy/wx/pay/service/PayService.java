package org.jfantasy.wx.pay.service;

import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Restrictions;
import org.jfantasy.framework.httpclient.HttpClientUtil;
import org.jfantasy.framework.httpclient.Request;
import org.jfantasy.framework.httpclient.Response;
import org.jfantasy.framework.spring.mvc.error.NotFoundException;
import org.jfantasy.framework.spring.mvc.error.RestException;
import org.jfantasy.framework.util.common.DateUtil;
import org.jfantasy.framework.util.common.MessageDigestUtil;
import org.jfantasy.framework.util.sax.XMLReader;
import org.jfantasy.framework.util.sax.XmlElement;
import org.jfantasy.framework.util.web.WebUtil;
import org.jfantasy.pay.bean.PayConfig;
import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.product.CurrencyType;
import org.jfantasy.pay.product.PayProduct;
import org.jfantasy.pay.product.WxpayProductSupport;
import org.jfantasy.pay.product.order.Order;
import org.jfantasy.pay.product.order.OrderService;
import org.jfantasy.pay.product.order.OrderServiceFactory;
import org.jfantasy.pay.service.PayConfigService;
import org.jfantasy.pay.service.PayProductConfiguration;
import org.jfantasy.pay.service.PaymentService;
import org.jfantasy.wx.bean.User;
import org.jfantasy.wx.service.UserService;
import org.jfantasy.wx.service.vo.PrePayment;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

@Service("weixin.payService")
@Transactional
public class PayService implements InitializingBean {

    private final static Log LOG = LogFactory.getLog(PayService.class);

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private OrderServiceFactory orderServiceFactory;
    @Autowired(required = false)
    private PayProductConfiguration payProductConfiguration;
    @Autowired
    private PayConfigService payConfigService;
    @Autowired
    private UserService userService;

    @Override
    public void afterPropertiesSet() throws Exception {
        //添加支付产品
        WxpayProductSupport wxpayProduct = new WxpayProductSupport();
        wxpayProduct.setId("weixinPay");
        wxpayProduct.setName("微信支付");
        wxpayProduct.setName("微信支付");
        wxpayProduct.setBargainorIdName("商户号");
        wxpayProduct.setBargainorKeyName("API密钥");
        wxpayProduct.setShroffAccountName("APPID");
        wxpayProduct.setCurrencyTypes(new CurrencyType[]{CurrencyType.CNY});
        wxpayProduct.setLogoPath("");
        wxpayProduct.setDescription("");
        payProductConfiguration.addPaymentProduct(wxpayProduct);
    }

    /**
     * 订单支付查询
     *
     * @param appid 微信id
     * @param sn    支付流水
     * @return PrePayment
     */
    public Payment query(String appid, String sn) {
        try {
            Payment payment = this.paymentService.get(sn);
            if (payment == null) {
                throw new NotFoundException("[sn=" + sn + "]对应的支付单不存在");
            }
            PayConfig paymentConfig = payment.getPayConfig();
            //组装数据
            Map<String, String> data = new TreeMap<String, String>();
            data.put("appid", appid);
            data.put("mch_id", paymentConfig.getBargainorId());
            data.put("out_trade_no", payment.getSn());
            data.put("nonce_str", generateNonceString(16));
            String params = "";
            String xml = "<xml>\n";
            for (Map.Entry<String, String> entry : data.entrySet()) {
                params += (entry.getKey() + "=" + entry.getValue() + "&");
                xml += ("\t<" + entry.getKey() + ">" + entry.getValue() + "</" + entry.getKey() + ">\n");
            }
            String sign = sign(params, paymentConfig.getBargainorKey());
            xml += ("\t<sign>" + sign + "</sign>\n</xml>");

            Response response = HttpClientUtil.doPost("https://api.mch.weixin.qq.com/pay/orderquery", new Request(new StringRequestEntity(xml, "text/xml", "utf-8")));
            LOG.debug("微信端响应:" + response.getText());
            //解析数据
            XmlElement xmlElement = XMLReader.reader(new ByteArrayInputStream(response.getText().getBytes()));
            assert xmlElement != null;
            data.clear();
            for (XmlElement node : xmlElement.getChildNodes()) {
                data.put(node.getTagName(), node.getContent());
            }
            //判断业务处理是否成功
            if (!"SUCCESS".equalsIgnoreCase(data.get("result_code"))) {
                throw new RestException(data.get("return_msg"));
            }
            //验证签名
            if (!sign(data, paymentConfig.getBargainorKey()).equalsIgnoreCase(data.get("sign"))) {
                throw new RestException("微信返回的响应签名错误");
            }
            String tradeNo = data.get("transaction_id");
            String state = data.get("trade_state");
            if ("SUCCESS".equalsIgnoreCase(state)) {//支付成功
                this.paymentService.success(payment.getSn(), tradeNo);
            } else if ("CLOSED".equalsIgnoreCase(state)) {//已关闭
                this.paymentService.close(payment.getSn(), tradeNo);
            } else if ("PAYERROR".equalsIgnoreCase(state) || "REFUND".equalsIgnoreCase(state) || "REVOKED".equalsIgnoreCase(state)) {
                //支付失败 and 转入退款 and 已撤销
                this.paymentService.failure(payment.getSn(), tradeNo, data.get("trade_state_desc"));
            }
            return payment;
        } catch (IOException e) {
            throw new RestException("调用微信接口,网络错误!");
        }
    }

    /**
     * 创建微信支付预订单
     *
     * @param appid      微信公众号
     * @param prePayment 预支付单
     * @return PrePayment
     */
    public PrePayment preOrder(String appid, PrePayment prePayment) {
        PayConfig paymentConfig = this.payConfigService.findUnique(Restrictions.eq("paymentProductId", "weixinPay"), Restrictions.eq("sellerEmail", appid));
        if (paymentConfig == null) {
            throw new RestException("[appid=" + appid + "]的支付配置未找到!");
        }
        //获取订单信息
        OrderService orderDetailsService = orderServiceFactory.getOrderService(prePayment.getOrderType());
        Order order = orderDetailsService.loadOrder(prePayment.getOrderSn());
        try {
            User user = userService.findUnique(Restrictions.eq("openId", prePayment.getOpenid()));
            Payment payment = null;//this.paymentService.ready(prePayment.getOrderType(), prePayment.getOrderSn(), user != null && user.getMember() != null ? user.getMember().getUsername() : prePayment.getOpenid(), paymentConfig.getId());
            PayProduct payProduct = null;//this.paymentService.getPayProduct(payment.getPayConfig().getPayProductId());
            //组装数据
            Map<String, String> data = new TreeMap<String, String>();
            data.put("appid", appid);
            data.put("mch_id", paymentConfig.getBargainorId());
            data.put("device_info", "WEB");
            data.put("body", order.getSubject());
            data.put("nonce_str", generateNonceString(16));
            data.put("openid", prePayment.getOpenid());
            data.put("trade_type", "JSAPI");
            data.put("notify_url", "http://wxpay.weixin.qq.com/pub_v2/pay/notify.v2.php");
            data.put("out_trade_no", payment.getSn());
            data.put("spbill_create_ip", WebUtil.getServerIps()[0]);
            data.put("total_fee", order.getPayableFee().multiply(BigDecimal.valueOf(100)).setScale(0, BigDecimal.ROUND_HALF_UP).intValue() + "");
            String params = "";
            String xml = "<xml>\n";
            for (Map.Entry<String, String> entry : data.entrySet()) {
                params += (entry.getKey() + "=" + entry.getValue() + "&");
                xml += ("\t<" + entry.getKey() + ">" + entry.getValue() + "</" + entry.getKey() + ">\n");
            }
            String sign = sign(params, paymentConfig.getBargainorKey());
            xml += ("\t<sign>" + sign + "</sign>\n</xml>");
            Response response = HttpClientUtil.doPost("", new Request(new StringRequestEntity(xml, "text/xml", "utf-8")));
            LOG.debug("微信端响应:" + response.getText());
            //解析数据
            XmlElement xmlElement = XMLReader.reader(new ByteArrayInputStream(response.getText().getBytes()));
            assert xmlElement != null;
            data.clear();
            for (XmlElement node : xmlElement.getChildNodes()) {
                data.put(node.getTagName(), node.getContent());
            }
            //判断业务处理是否成功
            if (!"SUCCESS".equalsIgnoreCase(data.get("result_code"))) {
                throw new RestException(data.get("return_msg"));
            }
            //验证签名
            if (!sign(data, paymentConfig.getBargainorKey()).equalsIgnoreCase(data.get("sign"))) {
                throw new RestException("微信返回的响应签名错误");
            }
            //返回用用JSPAPI使用的字段
            prePayment.setSn(payment.getSn());
            prePayment.setTimeStamp(DateUtil.now().getTime());
            prePayment.setNonceStr(generateNonceString(16));
            prePayment.setPackage("prepay_id=" + data.get("prepay_id"));
            prePayment.setSignType("MD5");
            //签名
            data.clear();
            data.put("appId", appid);
            data.put("timeStamp", prePayment.getTimeStamp() + "");
            data.put("nonceStr", prePayment.getNonceStr());
            data.put("package", prePayment.getPackage());
            data.put("signType", prePayment.getSignType());
            prePayment.setPaySign(sign(data, paymentConfig.getBargainorKey()));
            return prePayment;
        } catch (IOException e) {
            throw new RestException("调用微信接口,网络错误!");
        }/* catch (PayException e) {
            throw new RestException(e.getMessage());
        }*/
    }

    private static String sign(Map<String, String> data, String bargainorKey) {
        String str = "";
        Map<String, String> params = new TreeMap<String, String>(data);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if ("sign".equals(entry.getKey())) {
                continue;
            }
            str += (entry.getKey() + "=" + entry.getValue() + "&");
        }
        return sign(str, bargainorKey);
    }

    private static String sign(String data, String bargainorKey) {
        return MessageDigestUtil.getInstance().get(data + "key=" + bargainorKey).toUpperCase();
    }

    private static final String NONCE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String generateNonceString(int length) {
        int maxPos = NONCE_CHARS.length();
        String noceStr = "";
        for (int i = 0; i < length; i++) {
            noceStr += NONCE_CHARS.charAt((int) Math.floor(Math.random() * maxPos));
        }
        return noceStr;
    }

    public String notify(String appid, String xml) {
        try {
            Map<String, String> data = new TreeMap<String, String>();
            //解析数据
            XmlElement xmlElement = XMLReader.reader(new ByteArrayInputStream(xml.getBytes()));
            assert xmlElement != null;
            data.clear();
            for (XmlElement node : xmlElement.getChildNodes()) {
                data.put(node.getTagName(), node.getContent());
            }
            //判断业务处理是否成功
            if (!"SUCCESS".equalsIgnoreCase(data.get("result_code"))) {
                throw new RestException(data.get("return_msg"));
            }
            PayConfig paymentConfig = this.payConfigService.findUnique(Restrictions.eq("paymentProductId", "weixinPay"), Restrictions.eq("sellerEmail", appid));
            if (paymentConfig == null) {
                throw new RestException("[appid=" + appid + "]的支付配置未找到!");
            }
            //验证签名
            if (!sign(data, paymentConfig.getBargainorKey()).equalsIgnoreCase(data.get("sign"))) {
                throw new RestException("微信签名错误");
            }
            //判断业务处理是否成功
            if (!"SUCCESS".equalsIgnoreCase(data.get("result_code"))) {
                throw new RestException(data.get("return_msg"));
            }
            String paysn = data.get("out_trade_no");
            String tradeNo = data.get("transaction_id");
            String state = data.get("trade_state");
            if ("SUCCESS".equalsIgnoreCase(state)) {//支付成功
                this.paymentService.success(paysn, tradeNo);
            } else if ("CLOSED".equalsIgnoreCase(state)) {//已关闭
                this.paymentService.close(paysn, tradeNo);
            } else if ("PAYERROR".equalsIgnoreCase(state) || "REFUND".equalsIgnoreCase(state) || "REVOKED".equalsIgnoreCase(state)) {
                //支付失败 and 转入退款 and 已撤销
                this.paymentService.failure(paysn, tradeNo, data.get("trade_state_desc"));
            }
            return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
        } catch (RestException ex) {
            return "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[" + ex.getMessage() + "]]></return_msg></xml>";
        }
    }
}
