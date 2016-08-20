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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 财付通（担保交易）
 */

public class TenpayPartner extends PayProductSupport {

    public static final String PAYMENT_URL = "https://www.tenpay.com/cgi-bin/med/show_opentrans.cgi";// 支付请求URL
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
        String totalAmountString = paymentAmount.multiply(new BigDecimal(100)).setScale(0).toString();

        String attach = "sh" + "op" + "xx";// 商户数据
        String chnid = paymentConfig.getBargainorId();// 商户号
        String cmdno = "12";// 业务代码（12：担保交易支付）
        String encodeType = "2";// 字符集编码格式（1：GB2312、2：UTF-8）
        String mchDesc = "";// 订单描述
        String mchName = paymentSn;// 商品名称
        String mchPrice = totalAmountString;// 总金额（单位：分）
        String mchReturl = SettingUtil.getServerUrl() + RETURN_URL + "?paymentsn=" + paymentSn;// 回调处理URL
        String mchType = "1";// 交易类型（1、实物交易、2、虚拟交易）
        String mchVno = paymentSn;// 交易号
        String needBuyerinfo = "2";// 是否需要填写物流信息（1：需要、2：不需要）
        String seller = paymentConfig.getBargainorId();// 商户号
        String showUrl = SettingUtil.getServerUrl() + RETURN_URL + "?paymentsn=" + paymentSn;// 商品显示URL
        String transportDesc = "";// 物流方式说明
        String transportFee = "0";// 物流费用（单位：分）
        String version = "2";// 版本号
        String key = paymentConfig.getBargainorKey();// 密钥

        // 生成签名
        Map<String, String> signMap = new LinkedHashMap<String, String>();
        signMap.put("attach", attach);
        signMap.put("chnid", chnid);
        signMap.put("cmdno", cmdno);
        signMap.put("encode_type", encodeType);
        signMap.put("mch_desc", mchDesc);
        signMap.put("mch_name", mchName);
        signMap.put("mch_price", mchPrice);
        signMap.put("mch_returl", mchReturl);
        signMap.put("mch_type", mchType);
        signMap.put("mch_vno", mchVno);
        signMap.put("need_buyerinfo", needBuyerinfo);
        signMap.put("seller", seller);
        signMap.put("show_url", showUrl);
        signMap.put("transport_desc", transportDesc);
        signMap.put("transport_fee", transportFee);
        signMap.put("version", version);
        signMap.put("key", key);
        String sign = DigestUtils.md5Hex(SignUtil.coverMapString(signMap)).toUpperCase();

        // 参数处理
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("attach", attach);
        parameterMap.put("chnid", chnid);
        parameterMap.put("cmdno", cmdno);
        parameterMap.put("encode_type", encodeType);
        parameterMap.put("mch_desc", mchDesc);
        parameterMap.put("mch_name", mchName);
        parameterMap.put("mch_price", mchPrice);
        parameterMap.put("mch_returl", mchReturl);
        parameterMap.put("mch_type", mchType);
        parameterMap.put("mch_vno", mchVno);
        parameterMap.put("need_buyerinfo", needBuyerinfo);
        parameterMap.put("seller", seller);
        parameterMap.put("show_url", showUrl);
        parameterMap.put("transport_desc", transportDesc);
        parameterMap.put("transport_fee", transportFee);
        parameterMap.put("version", version);
        parameterMap.put("sign", sign);
        return parameterMap;
    }

    public boolean verifySign(Map<String, String> parameters) {
        PayConfig paymentConfig = null;//PaymentContext.getContext().getPaymentConfig();
        // 获取参数
        String attach = parameters.get("attach");
        String buyerId = parameters.get("buyer_id");
        String cftTid = parameters.get("cft_tid");
        String chnid = parameters.get("chnid");
        String cmdno = parameters.get("cmdno");
        String mchVno = parameters.get("mch_vno");
        String retcode = parameters.get("retcode");
        String seller = parameters.get("seller");
        String status = parameters.get("status");
        String totalFee = parameters.get("total_fee");
        String tradePrice = parameters.get("trade_price");
        String transportFee = parameters.get("transport_fee");
        String version = parameters.get("version");
        String sign = parameters.get("sign");

        // 验证支付签名
        Map<String, String> parameterMap = new LinkedHashMap<String, String>();
        parameterMap.put("attach", attach);
        parameterMap.put("buyer_id", buyerId);
        parameterMap.put("cft_tid", cftTid);
        parameterMap.put("chnid", chnid);
        parameterMap.put("cmdno", cmdno);
        parameterMap.put("mch_vno", mchVno);
        parameterMap.put("retcode", retcode);
        parameterMap.put("seller", seller);
        parameterMap.put("status", status);
        parameterMap.put("total_fee", totalFee);
        parameterMap.put("trade_price", tradePrice);
        parameterMap.put("transport_fee", transportFee);
        parameterMap.put("version", version);
        parameterMap.put("key", paymentConfig.getBargainorKey());
        return StringUtils.equals(sign, DigestUtils.md5Hex(SignUtil.coverMapString(parameterMap)).toUpperCase());
    }

    public String getPaynotifyMessage(String paymentSn) {
        return null;
    }

    public PayResult parsePayResult(Map<String, String> parameters) {
        //getPaymentSn
        //parameters.get("cft_tid")
        //getPaymentAmount
        //new BigDecimal(parameters.get("total_fee")).divide(new BigDecimal(100))
        String status = parameters.get("status");
        StringUtils.equals(status, "3");
        return null;
    }

    @Override
    public String web(Payment payment, Order order, Properties properties) {
        return null;
    }

    @Override
    public Payment payNotify(Payment payment,  String result) throws PayException{return null;
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