package com.fantasy.payment.product;

import com.fantasy.payment.bean.Payment;
import com.fantasy.payment.bean.PaymentConfig;
import com.fantasy.payment.service.PaymentContext;
import com.fantasy.system.util.SettingUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 财付通（担保交易）
 */

public class TenpayPartner extends AbstractPaymentProduct {

    public static final String PAYMENT_URL = "https://www.tenpay.com/cgi-bin/med/show_opentrans.cgi";// 支付请求URL
    public static final String RETURN_URL = "/shop/payment!payreturn.action";// 回调处理URL

    // 支持货币种类
    public static final CurrencyType[] currencyType = {CurrencyType.CNY};

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
        String totalAmountString = paymentAmount.multiply(new BigDecimal(100)).setScale(0).toString();

        String attach = "sh" + "op" + "xx";// 商户数据
        String chnid = paymentConfig.getBargainorId();// 商户号
        String cmdno = "12";// 业务代码（12：担保交易支付）
        String encode_type = "2";// 字符集编码格式（1：GB2312、2：UTF-8）
        String mch_desc = "";// 订单描述
        String mch_name = paymentSn;// 商品名称
        String mch_price = totalAmountString;// 总金额（单位：分）
        String mch_returl = SettingUtil.get("website", "ShopUrl") + RETURN_URL + "?paymentsn=" + paymentSn;// 回调处理URL
        String mch_type = "1";// 交易类型（1、实物交易、2、虚拟交易）
        String mch_vno = paymentSn;// 交易号
        String need_buyerinfo = "2";// 是否需要填写物流信息（1：需要、2：不需要）
        String seller = paymentConfig.getBargainorId();// 商户号
        String show_url = SettingUtil.get("website", "ShopUrl") + RETURN_URL + "?paymentsn=" + paymentSn;// 商品显示URL
        String transport_desc = "";// 物流方式说明
        String transport_fee = "0";// 物流费用（单位：分）
        String version = "2";// 版本号
        String key = paymentConfig.getBargainorKey();// 密钥

        // 生成签名
        Map<String, String> signMap = new LinkedHashMap<String, String>();
        signMap.put("attach", attach);
        signMap.put("chnid", chnid);
        signMap.put("cmdno", cmdno);
        signMap.put("encode_type", encode_type);
        signMap.put("mch_desc", mch_desc);
        signMap.put("mch_name", mch_name);
        signMap.put("mch_price", mch_price);
        signMap.put("mch_returl", mch_returl);
        signMap.put("mch_type", mch_type);
        signMap.put("mch_vno", mch_vno);
        signMap.put("need_buyerinfo", need_buyerinfo);
        signMap.put("seller", seller);
        signMap.put("show_url", show_url);
        signMap.put("transport_desc", transport_desc);
        signMap.put("transport_fee", transport_fee);
        signMap.put("version", version);
        signMap.put("key", key);
        String sign = DigestUtils.md5Hex(getParameterString(signMap)).toUpperCase();

        // 参数处理
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("attach", attach);
        parameterMap.put("chnid", chnid);
        parameterMap.put("cmdno", cmdno);
        parameterMap.put("encode_type", encode_type);
        parameterMap.put("mch_desc", mch_desc);
        parameterMap.put("mch_name", mch_name);
        parameterMap.put("mch_price", mch_price);
        parameterMap.put("mch_returl", mch_returl);
        parameterMap.put("mch_type", mch_type);
        parameterMap.put("mch_vno", mch_vno);
        parameterMap.put("need_buyerinfo", need_buyerinfo);
        parameterMap.put("seller", seller);
        parameterMap.put("show_url", show_url);
        parameterMap.put("transport_desc", transport_desc);
        parameterMap.put("transport_fee", transport_fee);
        parameterMap.put("version", version);
        parameterMap.put("sign", sign);
        return parameterMap;
    }

    @Override
    public boolean verifySign(Map<String, String> parameters) {
        PaymentConfig paymentConfig = PaymentContext.getContext().getPaymentConfig();
        // 获取参数
        String attach = parameters.get("attach");
        String buyer_id = parameters.get("buyer_id");
        String cft_tid = parameters.get("cft_tid");
        String chnid = parameters.get("chnid");
        String cmdno = parameters.get("cmdno");
        String mch_vno = parameters.get("mch_vno");
        String retcode = parameters.get("retcode");
        String seller = parameters.get("seller");
        String status = parameters.get("status");
        String total_fee = parameters.get("total_fee");
        String trade_price = parameters.get("trade_price");
        String transport_fee = parameters.get("transport_fee");
        String version = parameters.get("version");
        String sign = parameters.get("sign");

        // 验证支付签名
        Map<String, String> parameterMap = new LinkedHashMap<String, String>();
        parameterMap.put("attach", attach);
        parameterMap.put("buyer_id", buyer_id);
        parameterMap.put("cft_tid", cft_tid);
        parameterMap.put("chnid", chnid);
        parameterMap.put("cmdno", cmdno);
        parameterMap.put("mch_vno", mch_vno);
        parameterMap.put("retcode", retcode);
        parameterMap.put("seller", seller);
        parameterMap.put("status", status);
        parameterMap.put("total_fee", total_fee);
        parameterMap.put("trade_price", trade_price);
        parameterMap.put("transport_fee", transport_fee);
        parameterMap.put("version", version);
        parameterMap.put("key", paymentConfig.getBargainorKey());
        return StringUtils.equals(sign, DigestUtils.md5Hex(getParameterString(parameterMap)).toUpperCase());
    }

    @Override
    public String getPaynotifyMessage(String paymentSn) {
        return null;
    }

    @Override
    public PayResult parsePayResult(Map<String, String> parameters) {
        //getPaymentSn
        //parameters.get("cft_tid")
        //getPaymentAmount
        //new BigDecimal(parameters.get("total_fee")).divide(new BigDecimal(100))
        String status = parameters.get("status");
        StringUtils.equals(status, "3");
        return null;
    }
}