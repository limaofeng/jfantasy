package com.fantasy.payment.product;

import com.fantasy.framework.util.web.WebUtil;
import com.fantasy.payment.bean.Payment;
import com.fantasy.payment.bean.PaymentConfig;
import com.fantasy.common.order.Order;
import com.fantasy.payment.service.PaymentContext;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 支付宝（担保交易）
 */
public class AlipayPartner extends AbstractAlipayPaymentProduct {
    /**
     * 支付宝消息验证地址
     */
    public static final String PAYMENT_URL = "https://mapi.alipay.com/gateway.do?_input_charset=" + input_charset;// 支付请求URL

    @Override
    public String getPaymentUrl() {
        return PAYMENT_URL;
    }

    @Override
    public Map<String, String> getParameterMap(Map<String, String> parameters) {
        PaymentContext context = PaymentContext.getContext();
        PaymentConfig paymentConfig = context.getPaymentConfig();
        Order orderDetails = context.getOrderDetails();
        Payment payment = context.getPayment();

        String body = orderDetails.getSubject();// 订单描述
        String logisticsFee = "0";// 物流费用
        String logisticsPayment = "SELLER_PAY";// 物流支付方式（SELLER_PAY：卖家承担运费、BUYER_PAY：买家承担运费）
        String logisticsType = "EXPRESS";// 物流类型（EXPRESS：快递、POST：平邮、EMS：EMS）
        String notifyUrl = PaymentContext.getContext().getNotifyUrl(payment.getSn());// 消息通知URL
        AtomicReference<String> outTradeNo = new AtomicReference<String>(payment.getSn());// 支付编号
        String partner = paymentConfig.getBargainorId();// 合作身份者ID
        String paymentType = "1";// 支付类型（固定值：1）
        String price = String.format("%.2f", orderDetails.getPayableFee());// 总金额（单位：元）
        String quantity = "1";// 商品数量
        String returnUrl = PaymentContext.getContext().getReturnUrl(payment.getSn());// 回调处理URL
        String sellerId = paymentConfig.getSellerEmail();
        String service = "create_partner_trade_by_buyer";// 接口类型（create_partner_trade_by_buyer：担保交易）
        String showUrl = PaymentContext.getContext().getShowUrl(orderDetails.getSN());// 支付结果显示URL
        String signType = "MD5";//签名加密方式（MD5）
        AtomicReference<String> subject = new AtomicReference<String>(payment.getSn());// 订单的名称、标题、关键字等
        String key = paymentConfig.getBargainorKey();// 密钥

        //TODO 担保支付时的收货人信息如何设置
        String receiveName = "昊略软件";//收货人姓名
        String receiveAddress = "上海市徐汇区田林路140号28号楼G09";//收货人地址
        String receiveZip = "200233";//收货人邮编
        String receivePhone = "0571-88158090";//收货人电话号码
        String receiveMobile = "15921884771";//收货人手机号码

        // 生成签名
        Map<String, String> signMap = new LinkedHashMap<String, String>();

        signMap.put("service", service);
        signMap.put("partner", partner);
        signMap.put("_input_charset", input_charset);
        signMap.put("payment_type", paymentType);
        signMap.put("notify_url", notifyUrl);
        signMap.put("return_url", returnUrl);
        signMap.put("seller_email", sellerId);
        signMap.put("out_trade_no", outTradeNo.get());
        signMap.put("subject", subject.get());
        signMap.put("price", price);
        signMap.put("quantity", quantity);
        signMap.put("logistics_fee", logisticsFee);
        signMap.put("logistics_type", logisticsType);
        signMap.put("logistics_payment", logisticsPayment);
        signMap.put("body", body);
        signMap.put("show_url", showUrl);
        signMap.put("receive_name", receiveName);
        signMap.put("receive_address", receiveAddress);
        signMap.put("receive_zip", receiveZip);
        signMap.put("receive_phone", receivePhone);
        signMap.put("receive_mobile", receiveMobile);
        String sign = DigestUtils.md5Hex(getParameterString(signMap) + key);
        // 参数处理
        Map<String, String> parameterMap = new HashMap<String, String>(paraFilter(signMap));
        parameterMap.put("sign_type", signType);
        parameterMap.put("sign", sign);
        return parameterMap;
    }

    @Override
    public boolean verifySign(Map<String, String> parameters) {
        PaymentConfig paymentConfig = PaymentContext.getContext().getPaymentConfig();
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
        payResult.setStatus(StringUtils.equals("WAIT_SELLER_SEND_GOODS", params.get("trade_status")) ? PayResult.PayStatus.success : PayResult.PayStatus.failure);
        return payResult;
    }

}